package com.inspiral.inspiralbackend.security;

import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import org.springframework.stereotype.Component;

@Component
public class TrainerIdentifier {

    private TrainerRepository trainerRepository;
    private JwtValidator jwtValidator = new JwtValidator();

    public TrainerIdentifier(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer identify(String token) {
        Integer id = jwtValidator.validate(token.substring(7)).getId();
        return trainerRepository.findTrainerById(id);
    }
}

