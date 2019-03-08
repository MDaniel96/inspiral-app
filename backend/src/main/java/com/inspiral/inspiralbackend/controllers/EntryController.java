package com.inspiral.inspiralbackend.controllers;


import com.inspiral.inspiralbackend.config.MailSender;
import com.inspiral.inspiralbackend.models.Comment;
import com.inspiral.inspiralbackend.models.Entry;
import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.CommentRepository;
import com.inspiral.inspiralbackend.repositories.EntryRepository;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import com.inspiral.inspiralbackend.security.TrainerIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class EntryController {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    private TrainerIdentifier trainerIdentifier;
    private MailSender mailSender;

    public EntryController(TrainerIdentifier trainerIdentifier, MailSender mailSender) {
        this.trainerIdentifier = trainerIdentifier;
        this.mailSender = mailSender;
    }


    @PostMapping("/admin/entry")
    public @ResponseBody ResponseEntity<Object> addEntry(@RequestHeader("Authorization") String token, @RequestBody Entry newEntry) {

        Trainer trainer = trainerIdentifier.identify(token);

        newEntry.setTrainerid(trainer.getId());
        newEntry.setTrainername(trainer.getName());

        entryRepository.save(newEntry);
        return ResponseEntity.status(HttpStatus.OK).body(newEntry);
    }

    @GetMapping("/admin/entry")
    public @ResponseBody ResponseEntity<Object> getTrainersEntries(@RequestHeader("Authorization") String token) {
        Trainer trainer = trainerIdentifier.identify(token);
        return ResponseEntity.status(HttpStatus.OK).body(entryRepository.findAllByTrainerid(trainer.getId()));
    }

    @PutMapping("/admin/entry/{entryId}")
    public @ResponseBody ResponseEntity<Object> updateTrainersEntry(@PathVariable(value="entryId") Integer entryId, @RequestBody Entry newEntry) {

        Entry entry = entryRepository.getById(entryId);

        if (entry == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");

        entry.updateWithNotNull(newEntry);
        entryRepository.save(entry);
        return ResponseEntity.status(HttpStatus.OK).body(entry);
    }

    @DeleteMapping("/admin/entry/{entryId}")
    public @ResponseBody ResponseEntity<Object> deleteTrainersEntries(@PathVariable(value="entryId") Integer entryId) {

        Entry entry = entryRepository.getById(entryId);

        if (entry == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");

        List<Comment> deletedEntrysQustions = commentRepository.findAllByEntryid(entry.getId());
        commentRepository.deleteAll(deletedEntrysQustions);
        entryRepository.delete(entry);
        return ResponseEntity.status(HttpStatus.OK).body("Entry deleted");
    }

    @DeleteMapping("/admin/entry/comment/{commentId}")
    public @ResponseBody ResponseEntity<Object> deleteComment(@PathVariable(value="commentId") Integer commentId) {

        Comment comment = commentRepository.getById(commentId);

        if (comment == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");

        commentRepository.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted");
    }

    @PostMapping("/entry/{entryId}/comment")
    public @ResponseBody ResponseEntity<Object> addCommentToEntry(@PathVariable(value="entryId") Integer entryId, @RequestBody Comment newComment) {

        Entry entry = entryRepository.getById(entryId);

        if (entry == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");

        newComment.setEntryid(entry.getId());
        commentRepository.save(newComment);
        return ResponseEntity.status(HttpStatus.OK).body("Comment added");

    }

    @GetMapping("/entry/{entryId}/comment")
    public @ResponseBody ResponseEntity<Object> getEntrysComments(@PathVariable(value="entryId") Integer entryId) {

        Entry entry = entryRepository.getById(entryId);

        if (entry == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");

        return ResponseEntity.status(HttpStatus.OK).body(commentRepository.findAllByEntryid(entry.getId()));
    }

    @GetMapping("/entry/all")
    public @ResponseBody ResponseEntity<Object> getAllEntries() {
        return ResponseEntity.status(HttpStatus.OK).body(entryRepository.findAll());
    }
}
