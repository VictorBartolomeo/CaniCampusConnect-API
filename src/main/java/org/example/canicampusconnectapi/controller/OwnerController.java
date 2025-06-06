package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwnerSelf;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class OwnerController {

    private final UserDao userDao;
    protected OwnerDao ownerDao;

    @Autowired
    public OwnerController(OwnerDao ownerDao, UserDao userDao) {
        this.ownerDao = ownerDao;
        this.userDao = userDao;
    }

    @IsOwnerSelf
    @JsonView(OwnerViewDog.class)
    @GetMapping("/owner/{id}")
    public ResponseEntity<Owner> getOwner(@PathVariable Long id) {

        Optional<Owner> optionalOwner = ownerDao.findById(id);
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalOwner.get(), HttpStatus.OK);

    }

@IsClubOwner
    @GetMapping("/owners")
    public List<Owner> getAll() {
        return ownerDao.findAll();
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

    @IsOwner
    @PutMapping("/owner/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @RequestBody @Validated(Owner.OnUpdateFromOwner.class) Owner owner) {
        Optional<Owner> ownerOptional = ownerDao.findById(id);
        if (ownerOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        owner.setId(id);
        owner.setPassword(ownerOptional.get().getPassword());
        ownerDao.save(owner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
