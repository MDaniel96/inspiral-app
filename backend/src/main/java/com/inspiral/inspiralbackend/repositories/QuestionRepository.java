package com.inspiral.inspiralbackend.repositories;

import com.inspiral.inspiralbackend.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findAllByPollid(Integer pollid);

    Question getById(Integer id);

}
