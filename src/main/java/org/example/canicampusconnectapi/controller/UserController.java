package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserController {

    protected UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/user/{id}")

    public ResponseEntity<User> getUser(@PathVariable Long id) {

        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);

    }


    @GetMapping("/users")
    public List<User> getAll() {
        return userDao.findAll();
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userDao.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {

        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Put change tout l'objet
    @PutMapping("/user/{id}")
    // Patch change une partie de l'objet
//    @PatchMapping("/user/{id}")
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
