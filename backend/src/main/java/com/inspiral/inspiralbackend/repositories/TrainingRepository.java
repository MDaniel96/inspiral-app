package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findAll();

    List<Training> findAllByTrainerid(Integer trainerid);

    List<Object> findAllByContentContains(String keyword);
    List<Object> findAllByTitleContains(String keyword);

    Training getById(Integer id);
    void deleteById(Integer id);
}
