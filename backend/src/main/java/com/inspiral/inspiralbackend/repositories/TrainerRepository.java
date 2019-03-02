package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    List<Trainer> findAll();
}

