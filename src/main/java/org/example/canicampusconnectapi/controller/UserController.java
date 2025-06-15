
package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.service.user.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserController {


    private final UserDao userDao;
    private final String avatarDir = "./uploads/users/";

    public UserController(UserService userService, UserDao userDao) {
        this.userDao = userDao;
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
}