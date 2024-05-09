package com.semillero.ubuntu.services.impl;


import com.semillero.ubuntu.dto.contactMessage.ContactMessageRequestSave;
import com.semillero.ubuntu.dto.contactMessage.ContactMessageResponse;
import com.semillero.ubuntu.dto.contactMessage.ManagedCount;
import com.semillero.ubuntu.entities.ContactMessage;
import com.semillero.ubuntu.entities.MicroEmpEntity;
import com.semillero.ubuntu.exceptions.ContactMessageNotFound;
import com.semillero.ubuntu.exceptions.MicroemprendimientoNotFound;
import com.semillero.ubuntu.mapper.ContactMessageMapper;
import com.semillero.ubuntu.repositories.IContactMessageRepository;
import com.semillero.ubuntu.repositories.MicroEmpsRepository;
import com.semillero.ubuntu.services.IContactMessageService;
import com.semillero.ubuntu.services.IEmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactMessageServiceImpl implements IContactMessageService {

    private final IContactMessageRepository contactMessageRepository;
    private final MicroEmpsRepository microEmpsRepository;
    private final IEmailService emailService;

    private static final String MICROEMPRENDIMIENTO_NOT_FOUND = "Microemprendimiento no encontrado";
    private static final String MESSAGE_NOT_FOUND = "Mensaje de contacto no encontrado";


    /**
     * Buscar mensaje por ID de mensaje
     */
    @Override
    public ContactMessageResponse findById(int messageId) {
        return ContactMessageMapper.toResponse(contactMessageRepository.findById(messageId)
                .orElseThrow(() -> new ContactMessageNotFound(MESSAGE_NOT_FOUND)));
    }

    /**
     * Buscar Mensajes de cada microemprendimiento por su id
     */
    @Override
    public Page<ContactMessageResponse> findByMicroEmprendimientoId(int id, Pageable pageable) {
        // Verificar si existe el microemprendimiento
        if (!microEmpsRepository.existsById((long) id)) {
            throw new MicroemprendimientoNotFound(MICROEMPRENDIMIENTO_NOT_FOUND);
        }
        return ContactMessageMapper.toResponseList(contactMessageRepository
                .findByMicroemprendimientoId(id, pageable));
    }

    /**
     * Buscar todos los mensajes
     */
    @Override
    public Page<ContactMessageResponse> findAll(Pageable pageable) {
        return ContactMessageMapper.toResponseList(contactMessageRepository
                .findAll(pageable));
    }

    /**
     * Buscar Mensajes Gestionados
     */
    @Override
    public Page<ContactMessageResponse> findAllByManagedTrue(Pageable pageable) {
        return ContactMessageMapper.toResponseList(contactMessageRepository
                .findByManagedIsTrue(pageable));
    }

    /**
     * Buscar Mensajes No Gestionados
     */
    @Override
    public Page<ContactMessageResponse> findAllByManagedFalse(Pageable pageable) {
        return ContactMessageMapper.toResponseList(contactMessageRepository
                .findByManagedIsFalse(pageable));
    }

    /**
     * Guardar nuevo mensaje
     */
    @Override
    public void save(ContactMessageRequestSave request) {

        // Mapear de DTO a Entity
        ContactMessage contactMessage = ContactMessageMapper.toEntity(request);
        // Asignar atributos
        contactMessage.setManaged(false);
        contactMessage.setDatetime(LocalDateTime.now());

        try {
            contactMessageRepository.save(contactMessage);
        } catch (DataIntegrityViolationException ex) {
            if (!microEmpsRepository.existsById(contactMessage.getMicroemprendimiento().getId())) {
                throw new MicroemprendimientoNotFound(MESSAGE_NOT_FOUND);
            }
        }
    }

    /**
     * Cambiar entre estado "Gestionado" o "No gestionado.
     * Envía email de aviso al creador del micro emprendimiento con el mensaje de contacto."
     */
    // Esta función hay que mejorarla y hacerla límpia. Viola principios SOLID
    @Override
    public Map<String, String> changeStatus(int contactMessageId, boolean status)  {

        Map<String, String> responseMap = new HashMap<>();

        Optional<ContactMessage> contactMessage = contactMessageRepository.findById(contactMessageId);

        // Cambiar estado Gestionado o No gestionado
        if (contactMessage.isPresent()) {
            try {
                contactMessageRepository.changeStatus(contactMessageId, status);
                responseMap.put("Estado", "Estado actualizado correctamente!");
            } catch (Exception ex) {
                responseMap.put("Estado", "ERROR: El estado no se pudo actualizar");
                throw new RuntimeException("Error, no se pudo actualizar el estado");
            }

            // Enviar email de aviso al creador del micro emprendimiento
            if (status && !contactMessage.get().isManaged()) {
                try {
                    this.sendEmail(contactMessage.get());
                    responseMap.put("Email", "Email enviado correctamente!");
                } catch (Exception ex) {
                    responseMap.put("Email", "ERROR: El email no se pudo enviar");
                    log.error("Error al enviar correo de mensaje de contacto de: "
                            + contactMessage.get().getEmail() + " al microemprendimiento: "
                            + contactMessage.get().getMicroemprendimiento().getNombre() + "\n" +
                            "ID de ContactMessage: " + contactMessage.get().getId());
                }
            }
        } else {
            throw new ContactMessageNotFound(MESSAGE_NOT_FOUND);
        }

        return responseMap;

    }

    @Override
    public ManagedCount countManaged() {
        ManagedCount count = new ManagedCount();
        count.setManaged(contactMessageRepository.countByManagedIsTrue());
        count.setNotManaged(contactMessageRepository.countByManagedIsFalse());
        return count;
    }

    // ------------------------

    // Enviar email al creador del micro-emprendimiento cuando se gestiona el mensaje
    private void sendEmail(ContactMessage contactMessage) throws MessagingException {
        MicroEmpEntity microDB = microEmpsRepository
                .findById(contactMessage.getMicroemprendimiento().getId())
                .orElseThrow(() -> new MicroemprendimientoNotFound(MICROEMPRENDIMIENTO_NOT_FOUND));
        String email = microDB.getEmail();
        emailService.sendContactMessageEmail(email, contactMessage);
    }

}
