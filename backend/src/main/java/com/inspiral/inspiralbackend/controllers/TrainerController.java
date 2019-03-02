package com.inspiral.inspiralbackend.controllers;

import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private TrainerRepository trainerRepository;


    @PostMapping("/login")
    public @ResponseBody List<Trainer> loginTrainer() {
        return trainerRepository.findAll();
    }

}
