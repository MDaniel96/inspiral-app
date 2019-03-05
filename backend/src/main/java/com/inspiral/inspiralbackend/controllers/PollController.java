package com.inspiral.inspiralbackend.controllers;


import com.inspiral.inspiralbackend.models.Poll;
import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.PollRepository;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import com.inspiral.inspiralbackend.security.TrainerIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    private TrainerIdentifier trainerIdentifier;

    public PollController(TrainerIdentifier trainerIdentifier) {
        this.trainerIdentifier = trainerIdentifier;
    }


    @PostMapping("/admin/poll")
    public @ResponseBody ResponseEntity<Object> addPoll(@RequestHeader("Authorization") String token, @RequestBody Poll newPoll) {

        Trainer trainer = trainerIdentifier.identify(token);

        newPoll.setTrainerid(trainer.getId());
        newPoll.setTrainername(trainer.getName());

        pollRepository.save(newPoll);
        return ResponseEntity.status(HttpStatus.OK).body(newPoll);
    }

    @GetMapping("/admin/poll")
    public @ResponseBody ResponseEntity<Object> getTrainersPolls(@RequestHeader("Authorization") String token) {
        Trainer trainer = trainerIdentifier.identify(token);
        return ResponseEntity.status(HttpStatus.OK).body(pollRepository.findAllByTrainerid(trainer.getId()));
    }

    @PutMapping("/admin/poll/{pollId}")
    public @ResponseBody ResponseEntity<Object> updateTrainersPoll(@PathVariable(value="pollId") Integer pollId, @RequestBody Poll newPoll) {

        Poll poll = pollRepository.getById(pollId);

        if (poll == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");

        poll.updateWithNotNull(newPoll);
        pollRepository.save(poll);
        return ResponseEntity.status(HttpStatus.OK).body(poll);
    }

    @DeleteMapping("/admin/poll/{pollId}")
    public @ResponseBody ResponseEntity<Object> deleteTrainersPolls(@PathVariable(value="pollId") Integer pollId) {

        Poll poll = pollRepository.getById(pollId);

        if (poll == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");

        pollRepository.delete(poll);
        return ResponseEntity.status(HttpStatus.OK).body("Poll deleted");
    }

    @GetMapping("/poll/all")
    public @ResponseBody ResponseEntity<Object> getAllPolls() {
        return ResponseEntity.status(HttpStatus.OK).body(pollRepository.findAll());
    }
}
