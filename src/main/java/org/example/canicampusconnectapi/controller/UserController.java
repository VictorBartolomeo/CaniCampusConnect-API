
package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.dto.ChangePasswordDTO;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.user.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserController {

    private final UserService userService;
    private final UserDao userDao;
    private final String avatarDir = "./uploads/users/";

    public UserController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }


    @GetMapping("/users")
    public List<User> getAll() {
        return userDao.findAll();
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/avatar")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable Long id) {
        try {
            Optional<User> optionalUser = userDao.findById(id);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Chercher le fichier avec les extensions courantes
            String[] extensions = {"png", "jpg", "jpeg", "webp"};
            for (String ext : extensions) {
                String fileName = id + "." + ext;
                Path filePath = Paths.get(avatarDir).resolve(fileName);

                if (Files.exists(filePath)) {
                    Resource resource = new UrlResource(filePath.toUri());

                    if (resource.exists() && resource.isReadable()) {
                        String contentType = Files.probeContentType(filePath);
                        if (contentType == null) {
                            contentType = "image/png";
                        }

                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(contentType))
                                .body(resource);
                    }
                }
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userDao.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setId(id);
        userDao.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}