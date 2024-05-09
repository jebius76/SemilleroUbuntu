package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.tasks.SendEmailWeekly;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emails")
public class TestEmailController {

    /** Este Controller es para testear los emails semanales.
     * Los emails se envían automáticamente sin necesidad de ejecutarlo manualmente */

    private final SendEmailWeekly sendEmailWeekly;

//    @GetMapping("/send")
    public ResponseEntity<Void> sendEmail() throws MessagingException {
        sendEmailWeekly.sendEmail();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
