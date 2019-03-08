package com.inspiral.inspiralbackend.controllers;

import com.inspiral.inspiralbackend.repositories.EntryRepository;
import com.inspiral.inspiralbackend.repositories.PollRepository;
import com.inspiral.inspiralbackend.repositories.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
public class GlobalController {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private TrainingRepository trainingRepository;

    @GetMapping("/search/{keyword}")
    public @ResponseBody ResponseEntity<Object> searchForKeyword(@PathVariable(value="keyword") String keyword) {
        Set<Object> result = new HashSet<>();

        result.addAll(entryRepository.findAllByContentContains(keyword));
        result.addAll(entryRepository.findAllByTitleContains(keyword));

        result.addAll(trainingRepository.findAllByContentContains(keyword));
        result.addAll(trainingRepository.findAllByTitleContains(keyword));


        result.addAll(pollRepository.findAllByTitleContains(keyword));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
