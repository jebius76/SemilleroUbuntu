package com.semillero.ubuntu.services;

import com.semillero.ubuntu.dto.PublicationDto;

import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PublicationService {
    @Transactional
    ResponseEntity<?> showAll();

    @Transactional
    ResponseEntity<?> getPublicationById(Long id);

    @Transactional
    ResponseEntity<?> createPublication(PublicationDto publicationDto);

    @Transactional
    ResponseEntity<?> updatePublication(PublicationDto publicationDto, Long id);

    @Transactional
    ResponseEntity<?> updateViews(Long id);

    @Transactional
    ResponseEntity<?> isActivoOrNot(Long id, boolean visible);

}

