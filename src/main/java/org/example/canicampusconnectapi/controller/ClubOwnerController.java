package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.ClubOwnerDao;
import org.example.canicampusconnectapi.model.users.ClubOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ClubOwnerController {

    protected ClubOwnerDao clubOwnerDao;

    @Autowired
    public ClubOwnerController(ClubOwnerDao clubOwnerDao) {
        this.clubOwnerDao = clubOwnerDao;
    }

    @GetMapping("/clubOwner/{id}")

    public ResponseEntity<ClubOwner> getClubOwner(@PathVariable Long id) {

        Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findById(id);
        if (optionalClubOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalClubOwner.get(), HttpStatus.OK);

    }


    @GetMapping("/clubOwners")
    public List<ClubOwner> getAll() {
        return clubOwnerDao.findAll();
    }

    @PostMapping("/clubOwner")
    public ResponseEntity<ClubOwner> createClubOwner(@RequestBody ClubOwner clubOwner) {
        clubOwnerDao.save(clubOwner);
        return new ResponseEntity<>(clubOwner, HttpStatus.CREATED);
    }

    @DeleteMapping("clubOwner/{id}")
    public ResponseEntity<ClubOwner> deleteClubOwner(@PathVariable Long id) {

        Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findById(id);
        if (optionalClubOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        clubOwnerDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Put change tout l'objet
    @PutMapping("/clubOwner/{id}")
    // Patch change une partie de l'objet
//    @PatchMapping("/clubOwner/{id}")
    public ResponseEntity<ClubOwner> updateClubOwner(@PathVariable Long id, @RequestBody ClubOwner clubOwner) {
        Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findById(id);
        if (optionalClubOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        clubOwner.setId(id);
        clubOwnerDao.save(clubOwner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
