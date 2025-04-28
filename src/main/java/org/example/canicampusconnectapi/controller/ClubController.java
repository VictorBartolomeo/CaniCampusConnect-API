package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.ClubDao;
import org.example.canicampusconnectapi.dao.ClubOwnerDao;
import org.example.canicampusconnectapi.model.Club;
import org.example.canicampusconnectapi.model.users.ClubOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ClubController {

    protected ClubDao clubDao;
    protected ClubOwnerDao clubOwnerDao;

    @Autowired
    public ClubController(ClubDao clubDao, ClubOwnerDao clubOwnerDao) {
        this.clubDao = clubDao;
        this.clubOwnerDao = clubOwnerDao;
    }

    @GetMapping("/club/{id}")
    public ResponseEntity<Club> getClub(@PathVariable Integer id) {
        Optional<Club> optionalClub = clubDao.findById(id);
        if (optionalClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalClub.get(), HttpStatus.OK);
    }

    @GetMapping("/clubs")
    public List<Club> getAllClubs() {
        return clubDao.findAll();
    }

    @GetMapping("/clubowner/{ownerId}/clubs")
    public ResponseEntity<List<Club>> getClubsByOwner(@PathVariable Long ownerId) {
        Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findById(ownerId);
        if (optionalClubOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Club> clubs = clubDao.findByClubOwner(optionalClubOwner.get());
        return new ResponseEntity<>(clubs, HttpStatus.OK);
    }

    @PostMapping("/club")
    public ResponseEntity<Club> createClub(@RequestBody Club club) {
        // Verify that the club owner exists
        if (club.getClubOwner() == null || club.getClubOwner().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findById(club.getClubOwner().getId());
        if (optionalClubOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        club.setClubOwner(optionalClubOwner.get());
        clubDao.save(club);
        return new ResponseEntity<>(club, HttpStatus.CREATED);
    }

    @DeleteMapping("/club/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Integer id) {
        Optional<Club> optionalClub = clubDao.findById(id);
        if (optionalClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        clubDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/club/{id}")
    public ResponseEntity<Void> updateClub(@PathVariable Integer id, @RequestBody Club club) {
        Optional<Club> optionalClub = clubDao.findById(id);
        if (optionalClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the club owner exists if it's being updated
        if (club.getClubOwner() != null && club.getClubOwner().getId() != null) {
            Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findById(club.getClubOwner().getId());
            if (optionalClubOwner.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            club.setClubOwner(optionalClubOwner.get());
        } else {
            // Keep the existing club owner if not provided
            club.setClubOwner(optionalClub.get().getClubOwner());
        }
        
        club.setId(id);
        clubDao.save(club);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}