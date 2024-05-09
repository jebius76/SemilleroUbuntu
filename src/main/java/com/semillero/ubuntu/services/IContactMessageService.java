package com.semillero.ubuntu.services;

import com.semillero.ubuntu.dto.contactMessage.ContactMessageRequestSave;
import com.semillero.ubuntu.dto.contactMessage.ContactMessageResponse;
import com.semillero.ubuntu.dto.contactMessage.ManagedCount;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;


public interface IContactMessageService {

    ContactMessageResponse findById(int messageId);

    Page<ContactMessageResponse> findByMicroEmprendimientoId(int id, Pageable pageable);

    Page<ContactMessageResponse> findAll(Pageable pageable);

    Page<ContactMessageResponse> findAllByManagedTrue(Pageable pageable);

    Page<ContactMessageResponse> findAllByManagedFalse(Pageable pageable);

    void save(ContactMessageRequestSave request);

    Map<String, String> changeStatus(int contactMessageId, boolean status) throws MessagingException;

    ManagedCount countManaged();

}