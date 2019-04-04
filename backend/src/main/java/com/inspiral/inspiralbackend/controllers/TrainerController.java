package com.inspiral.inspiralbackend.controllers;

import com.inspiral.inspiralbackend.models.JwtTrainer;
import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import com.inspiral.inspiralbackend.security.JwtGenerator;
import com.inspiral.inspiralbackend.security.JwtValidator;
import com.inspiral.inspiralbackend.security.TrainerIdentifier;
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
    private TrainerIdentifier trainerIdentifier;

    public TrainerController(JwtGenerator jwtGenerator, TrainerIdentifier trainerIdentifier) {
        this.jwtGenerator = jwtGenerator;
        this.trainerIdentifier = trainerIdentifier;
    }

    @GetMapping("*/aaa/*")
    public String helloWorld() {
        return "helloOOOOO";
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

        Trainer trainer = trainerIdentifier.identify(token);

        return ResponseEntity.status(HttpStatus.OK).body(trainer);
    }


    @PutMapping("/admin/trainer")
    public @ResponseBody ResponseEntity<Object> updateLoggedinTrainer(@RequestHeader("Authorization") String token, @RequestBody Trainer newTrainer) {

        Trainer trainer = trainerIdentifier.identify(token);

        trainer.updateWithNotNull(newTrainer);
        trainerRepository.updateTrainerById(trainer.getName(), trainer.getEmail(), trainer.getPassword(), trainer.getImage(), trainer.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Update OK");
    }

}
