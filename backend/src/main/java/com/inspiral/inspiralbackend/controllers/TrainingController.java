package com.inspiral.inspiralbackend.controllers;

import com.inspiral.inspiralbackend.config.MailSender;
import com.inspiral.inspiralbackend.models.EmailMessage;
import com.inspiral.inspiralbackend.models.Trainer;
import com.inspiral.inspiralbackend.models.Training;
import com.inspiral.inspiralbackend.repositories.TrainerRepository;
import com.inspiral.inspiralbackend.repositories.TrainingRepository;
import com.inspiral.inspiralbackend.security.TrainerIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@CrossOrigin
@RestController
public class TrainingController {

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    private TrainerIdentifier trainerIdentifier;
    private MailSender mailSender;

    public TrainingController(TrainerIdentifier trainerIdentifier, MailSender mailSender) {
        this.trainerIdentifier = trainerIdentifier;
        this.mailSender = mailSender;
    }


    @PostMapping("/admin/training")
    public @ResponseBody ResponseEntity<Object> addTraining(@RequestHeader("Authorization") String token, @RequestBody Training newTraining) {

        Trainer trainer = trainerIdentifier.identify(token);

        newTraining.setTrainerid(trainer.getId());
        newTraining.setTrainername(trainer.getName());

        trainingRepository.save(newTraining);
        return ResponseEntity.status(HttpStatus.OK).body(newTraining);
    }

    @GetMapping("/admin/training")
    public @ResponseBody ResponseEntity<Object> getTrainersTrainings(@RequestHeader("Authorization") String token) {
        Trainer trainer = trainerIdentifier.identify(token);
        return ResponseEntity.status(HttpStatus.OK).body(trainingRepository.findAllByTrainerid(trainer.getId()));
    }

    @PutMapping("/admin/training/{trainingId}")
    public @ResponseBody ResponseEntity<Object> updateTrainersTraining(@PathVariable(value="trainingId") Integer trainingId, @RequestBody Training newTraining) {

        Training training = trainingRepository.getById(trainingId);

        if (training == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training not found");

        training.updateWithNotNull(newTraining);
        trainingRepository.save(training);
        return ResponseEntity.status(HttpStatus.OK).body(training);
    }

    @DeleteMapping("/admin/training/{trainingId}")
    public @ResponseBody ResponseEntity<Object> deleteTrainersTrainings(@PathVariable(value="trainingId") Integer trainingId) {

        Training training = trainingRepository.getById(trainingId);

        if (training == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training not found");

        trainingRepository.delete(training);
        return ResponseEntity.status(HttpStatus.OK).body("Training deleted");
    }

    @GetMapping("/training/all")
    public @ResponseBody ResponseEntity<Object> getAllTrainings() {
        return ResponseEntity.status(HttpStatus.OK).body(trainingRepository.findAll());
    }

    @PostMapping("/training/{trainingId}/apply/{email}")
    public @ResponseBody ResponseEntity<Object> applyForTraining(@PathVariable(value="trainingId") Integer trainingId,
                                                                 @PathVariable(value="email") String fromEmail) throws MessagingException, IOException {
        Training training = trainingRepository.getById(trainingId);

        if (training == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training not found");

        Trainer toTrainer = trainerRepository.findTrainerById(training.getTrainerid());

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo_address(toTrainer.getEmail());
        emailMessage.setSubject("inspiralcoaching.hu: új training jelentkezés");
        emailMessage.setBody(setBody(fromEmail, training));

        mailSender.sendmail(emailMessage);

        return ResponseEntity.status(HttpStatus.OK).body("Email sent");
    }

    public String setBody(String fromEmail, Training training) {
        String body = new String();

        body = "Kedves " + training.getTrainername() + "!" + "<br>" + "<br>" + "<br>";
        body += "<b>" + fromEmail + "</b>" + " című látogató jelentkezett a " + "<b>" + training.getTitle() + "</b>"
                + " című trainingedre." + "<br>" + "<br>";
        body += "<br><br>Üdv,<br>inspiralcoaching.hu";
        return body;
    }
}
