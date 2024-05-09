package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.entities.ContactMessage;
import com.semillero.ubuntu.services.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String myEmail;

    @Override
    public void sendContactMessageEmail(String to, ContactMessage contactMessage) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        helper.setFrom(myEmail);
        helper.setTo(to);
        helper.setSubject("¡Recibiste un mensaje de contacto sobre tu Micro Emprendimiento!");
        helper.setText(
                "¡Hola Recibiste una solicitud de contacto acerca de tu Micro-emprendimiento: "
                        + contactMessage.getMicroemprendimiento().getNombre() + ".\n\n\n" +
                        "Nombre: " + contactMessage.getFullName() + "\n\n" +
                        "Correo: " + contactMessage.getEmail() + "\n\n" +
                        "Teléfono: " + contactMessage.getPhone() + "\n\n" +
                        "Mensaje: " + contactMessage.getMessage() + "\n\n"
        );
        mailSender.send(mimeMessage);
        log.info("Email enviado!");
    }

}
