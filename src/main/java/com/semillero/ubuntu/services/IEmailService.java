package com.semillero.ubuntu.services;

import com.semillero.ubuntu.entities.ContactMessage;
import jakarta.mail.MessagingException;

public interface IEmailService {

    void sendContactMessageEmail(String to, ContactMessage contactMessage) throws MessagingException;

}
