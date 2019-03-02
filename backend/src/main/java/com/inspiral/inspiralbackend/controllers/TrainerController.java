package com.inspiral.inspiralbackend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/trainer")
public class TrainerController {


    @PostMapping("/login")
    public String loginTrainer() {
        return "Hello";
    }

}
