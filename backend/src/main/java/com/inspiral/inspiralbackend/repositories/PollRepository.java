package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PollRepository extends JpaRepository<Poll, Integer> {

    List<Poll> findAll();

    List<Poll> findAllByTrainerid(Integer trainerid);

    List<Object> findAllByTitleContains(String keyword);

    Poll getById(Integer id);
    void deleteById(Integer id);

}
