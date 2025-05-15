package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.model.users.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class OwnerController {

    protected OwnerDao ownerDao;

    @Autowired
    public OwnerController(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<Owner> getOwner(@PathVariable Long id) {

        Optional<Owner> optionalOwner = ownerDao.findById(id);
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalOwner.get(), HttpStatus.OK);

    }


    @GetMapping("/owners")
    public List<Owner> getAll() {
        return ownerDao.findAll();
    }

    @PostMapping("/owner")
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        ownerDao.save(owner);
        return new ResponseEntity<>(owner, HttpStatus.CREATED);
    }

    @DeleteMapping("owner/{id}")
    public ResponseEntity<Owner> deleteOwner(@PathVariable Long id) {

        Optional<Owner> optionalOwner = ownerDao.findById(id);
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ownerDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Put change tout l'objet
    @PutMapping("/owner/{id}")
    // Patch change une partie de l'objet
//    @PatchMapping("/owner/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner owner) {
        Optional<Owner> optionalOwner = ownerDao.findById(id);
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        owner.setId(id);
        ownerDao.save(owner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
