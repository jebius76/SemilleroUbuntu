package com.semillero.ubuntu.tasks;

import com.semillero.ubuntu.entities.MicroEmpEntity;
import com.semillero.ubuntu.entities.PublicationEntity;
import com.semillero.ubuntu.repositories.IContactMessageRepository;
import com.semillero.ubuntu.repositories.MicroEmpsRepository;
import com.semillero.ubuntu.repositories.PublicationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendEmailWeekly {

    private static final Logger log = LoggerFactory.getLogger(SendEmailWeekly.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private final IContactMessageRepository contactMessageRepository;
    private final PublicationRepository publicationRepository;
    private final MicroEmpsRepository microEmpsRepository;

    @Value("${spring.mail.username}")
    private String myEmail;

    @Async
    @Scheduled(cron = "0 0 9 ? * MON") // Este método se ejecutará todos los lunes a las 9:00 AM
    public void sendEmail() throws MessagingException {

        try {
            List<String> emails = this.getEmails(); // Obtener emails de los usuarios
            if (emails.isEmpty()) return;

            List<PublicationEntity> publications = this.getPublications(); // Obtener todas las publicaciones de los últimos 7 días

            List<MicroEmpEntity> microemps = this.getMicroemps(); // Obtener todos los microEmprendimientos de los últimos 7 días

            if (publications.isEmpty() && microemps.isEmpty()) return;

        /* Crear un objeto Context de Thymeleaf y establecer las variables
         "publications" y "microEmprendimientos" en ese contexto */
            Context context = new Context();
            context.setVariable("publications", publications);
            context.setVariable("microemprendimientos", microemps);

            // Procesar la plantilla Thymeleaf y enviarle el Context
            String htmlContent = templateEngine.process("email_template", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(myEmail);

            // Enviar a toda la lista de usuarios. Descomentar al cambiar el servicio de email
            helper.setTo(emails.toArray(new String[0]));

            // Enviar a 1 solo mail. La versión de prueba solo me permite enviarme mails a mi mismo. Este correo es desechable
//        helper.setTo("ficehoh162@buzblox.com"); // Enviar a 1 solo mail

            helper.setSubject("Nuevas novedades de la semana!");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            log.info("Emails enviados correctamente a {}", emails);
        } catch (Exception ex) {
            log.error("Error al enviar emails semanales");
        }
    }

    private List<String> getEmails() {
        return contactMessageRepository.findAllEmails();
    }

    private List<PublicationEntity> getPublications() {
        return publicationRepository
                .findByCreationDateBetween(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    private List<MicroEmpEntity> getMicroemps() {
        return microEmpsRepository
                .findByCreationDateBetween(LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

}
