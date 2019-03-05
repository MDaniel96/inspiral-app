package com.inspiral.inspiralbackend.controllers;


import com.inspiral.inspiralbackend.config.MailSender;
import com.inspiral.inspiralbackend.models.EmailMessage;
import com.inspiral.inspiralbackend.models.Poll;
import com.inspiral.inspiralbackend.models.Question;
import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.repositories.PollRepository;
import com.inspiral.inspiralbackend.repositories.QuestionRepository;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import com.inspiral.inspiralbackend.security.TrainerIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
public class PollController {

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    private TrainerIdentifier trainerIdentifier;
    private MailSender mailSender;

    public PollController(TrainerIdentifier trainerIdentifier, MailSender mailSender) {
        this.trainerIdentifier = trainerIdentifier;
        this.mailSender = mailSender;
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

        List<Question> deletedPollsQuestions = questionRepository.findAllByPollid(poll.getId());
        questionRepository.deleteAll(deletedPollsQuestions);
        pollRepository.delete(poll);
        return ResponseEntity.status(HttpStatus.OK).body("Poll deleted");
    }

    @DeleteMapping("/admin/poll/question/{questionId}")
    public @ResponseBody ResponseEntity<Object> deleteQuestion(@PathVariable(value="questionId") Integer questionId) {

        Question question = questionRepository.getById(questionId);

        if (question == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");

        questionRepository.delete(question);
        return ResponseEntity.status(HttpStatus.OK).body("Question deleted");
    }

    @PostMapping("/admin/poll/{pollId}/question")
    public @ResponseBody ResponseEntity<Object> addQuestionToPoll(@PathVariable(value="pollId") Integer pollId, @RequestBody Question newQuestion) {

        Poll poll = pollRepository.getById(pollId);

        if (poll == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");

        newQuestion.setPollid(poll.getId());
        questionRepository.save(newQuestion);
        return ResponseEntity.status(HttpStatus.OK).body("Question added");
    }

    @GetMapping("/poll/{pollId}/question")
    public @ResponseBody ResponseEntity<Object> getPollsQuestions(@PathVariable(value="pollId") Integer pollId) {

        Poll poll = pollRepository.getById(pollId);

        if (poll == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");

        return ResponseEntity.status(HttpStatus.OK).body(questionRepository.findAllByPollid(poll.getId()));
    }

    @GetMapping("/poll/all")
    public @ResponseBody ResponseEntity<Object> getAllPolls() {
        return ResponseEntity.status(HttpStatus.OK).body(pollRepository.findAll());
    }

    @PostMapping("/poll/{pollId}/submit/{email}")
    public @ResponseBody ResponseEntity<Object> submitPoll(@PathVariable(value="pollId") Integer pollId,
                                                           @RequestBody List<String> answers,
                                                           @PathVariable(value="email") String fromEmail) throws AddressException, MessagingException, IOException {
        Poll poll = pollRepository.getById(pollId);

        if (poll == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");

        Trainer toTrainer = trainerRepository.findTrainerById(poll.getTrainerid());

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo_address(toTrainer.getEmail());
        emailMessage.setSubject("inspiralcoaching.hu: új kitöltött kérdőív");
        emailMessage.setBody(setBody(fromEmail, poll, answers));

        mailSender.sendmail(emailMessage);

        return ResponseEntity.status(HttpStatus.OK).body("Email sent");
    }


    public String setBody(String fromEmail, Poll poll, List<String> answers) {
        String body = new String();

        body = "Kedves " + poll.getTrainername() + "!" + "<br>" + "<br>" + "<br>";
        body += "<b>" + fromEmail + "</b>" + " című látogató kitöltötte a " + "<b>" + poll.getTitle() + "</b>"
                + " című kérdőívedet." + "<br>" + "<br>";

        List<Question> questions = questionRepository.findAllByPollid(poll.getId());
        for (Question q : questions)
            body += "<i>" + q.getContent() + "</i>" + "<br>" + answers.get(questions.indexOf(q)) + "<br>" + "<br>";

        body += "<br><br>Üdv,<br>inspiralcoaching.hu";

        return body;
    }

}
