package com.inspiral.inspiralbackend.controllers;

import com.inspiral.inspiralbackend.models.JwtTrainer;
import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import com.inspiral.inspiralbackend.security.JwtGenerator;
import com.inspiral.inspiralbackend.security.JwtValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
public class TrainerController {

    @Autowired
    private TrainerRepository trainerRepository;

    private JwtGenerator jwtGenerator;
    private JwtValidator jwtValidator;

    public TrainerController(JwtGenerator jwtGenerator, JwtValidator jwtValidator) {
        this.jwtGenerator = jwtGenerator;
        this.jwtValidator = jwtValidator;
    }

    @PostMapping("/trainer/login")
    public @ResponseBody ResponseEntity<Object> trainerLogin(@RequestBody final JwtTrainer jwtTrainer) {

        Trainer trainer = trainerRepository.findByEmailAndPassword(jwtTrainer.getEmail(), jwtTrainer.getPassword());
        if (trainer == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or pass");

        jwtTrainer.setId(trainer.getId());
        return ResponseEntity.status(HttpStatus.OK).body(jwtGenerator.generate(jwtTrainer));
    }


    @GetMapping("/admin/trainer")
    public @ResponseBody ResponseEntity<Object> getLoggedinTrainer(@RequestHeader("Authorization") String token) {

        Integer id = jwtValidator.validate(token.substring(7)).getId();

        Trainer trainer = trainerRepository.findTrainerById(id);
        if (trainer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found!");

        return ResponseEntity.status(HttpStatus.OK).body(trainer);
    }


    @PutMapping("/admin/trainer")
    public @ResponseBody ResponseEntity<Object> updateLoggedinTrainer(@RequestHeader("Authorization") String token, @RequestBody Trainer newTrainer) {

        Integer id = jwtValidator.validate(token.substring(7)).getId();

        Trainer trainer = trainerRepository.findTrainerById(id);
        if (trainer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found!");

        trainer.updateWithNotNull(newTrainer);
        trainerRepository.updateTrainerById(trainer.getName(), trainer.getEmail(), trainer.getPassword(), trainer.getImage(), id);
        return ResponseEntity.status(HttpStatus.OK).body("Update OK");
    }

}
