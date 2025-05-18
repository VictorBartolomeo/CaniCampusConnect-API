package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.view.owner.OwnerView;
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

    @IsOwner
    @JsonView(OwnerView.class)
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

    @IsOwner
    @JsonView(OwnerView.class)
    @PostMapping("/owner")
    public ResponseEntity<Owner> createOwner(@RequestBody @Validated(User.OnCreate.class) Owner owner) {
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

    //TODO Je dois renvoyer un user ou un owner ?
    // Il semble que Owner passe puisqu'il me dit bien que password en erreur donc comprend bien l'héritage, mais le validated ne passe pas,
    // j'ai déjà essayé de mettre l'interface dans Owner et dans user mais rien n'y fait il me demande le mot de passe à chaque fois
    @PutMapping("/owner/{id}")
    public ResponseEntity<User> updateOwner(@PathVariable Long id, @RequestBody @Validated(Owner.OnUpdateFromOwner.class) User owner) {
        Optional<User> user = userDao.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        owner.setId(id);
        userDao.save(owner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
