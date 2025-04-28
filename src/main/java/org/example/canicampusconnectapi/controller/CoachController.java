package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.model.users.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CoachController {

    protected CoachDao coachDao;

    @Autowired
    public CoachController(CoachDao coachDao) {
        this.coachDao = coachDao;
    }

    @GetMapping("/coach/{id}")

    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {

        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalCoach.get(), HttpStatus.OK);

    }


    @GetMapping("/coachs")
    public List<Coach> getAll() {
        return coachDao.findAll();
    }

    @PostMapping("/coach")
    public ResponseEntity<Coach> createCoach(@RequestBody Coach coach) {
        coachDao.save(coach);
        return new ResponseEntity<>(coach, HttpStatus.CREATED);
    }

    @DeleteMapping("coach/{id}")
    public ResponseEntity<Coach> deleteCoach(@PathVariable Long id) {

        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        coachDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Put change tout l'objet
    @PutMapping("/coach/{id}")
    // Patch change une partie de l'objet
//    @PatchMapping("/coach/{id}")
    public ResponseEntity<Coach> updateCoach(@PathVariable Long id, @RequestBody Coach coach) {
        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        coach.setId(id);
        coachDao.save(coach);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
