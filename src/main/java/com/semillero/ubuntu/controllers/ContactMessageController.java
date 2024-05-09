package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.dto.contactMessage.ContactMessageRequestSave;
import com.semillero.ubuntu.dto.contactMessage.ContactMessageResponse;
import com.semillero.ubuntu.services.IContactMessageService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/contactMessages")
@RequiredArgsConstructor
public class ContactMessageController {

    private final IContactMessageService messageService;

    /**
     * Buscar todos los mensajes
     */
    @GetMapping
    public ResponseEntity<Page<ContactMessageResponse>> findAll(@RequestParam int page,
                                                                @RequestParam int size) {

        Pageable pageable = setPagination(page, size);

        return new ResponseEntity<>(messageService.findAll(pageable), HttpStatus.OK);
    }

    /**
     * Buscar Mensaje por su ID
     */
    @GetMapping("/admin/{id}")
    public ResponseEntity<ContactMessageResponse> findMessageById(@PathVariable int id) {
        return new ResponseEntity<>(messageService.findById(id), HttpStatus.OK);
    }

    /**
     * Buscar Mensajes Gestionados o No gestionados
     *
     * @param status: recibe el estado de los mensajes: TRUE o FALSE
     * @param page:   es la página que se quiere ver
     * @param size:   el tamaño de cada página
     * @return lista de los mensajes con paginación
     */
    @GetMapping("/admin/managed")
    public ResponseEntity<Page<ContactMessageResponse>> findAllManaged(@RequestParam boolean status,
                                                                       @RequestParam int page,
                                                                       @RequestParam int size) {
        Page<ContactMessageResponse> messages;
        Pageable pageable = setPagination(page, size);

        if (status)
            messages = messageService.findAllByManagedTrue(pageable);
        else
            messages = messageService.findAllByManagedFalse(pageable);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    /**
     * Buscar Mensajes por Microemprendimiento por su ID
     *
     * @param id:   el ID del microemprendimiento
     * @param page: es la página que se quiere ver
     * @param size: el tamaño de cada página
     * @return lista de los mensajes de cada microemprendimiento con paginación
     */
    @GetMapping("/microemprendimiento/{id}")
    public ResponseEntity<Page<ContactMessageResponse>> findByMicroempId(@PathVariable int id,
                                                                         @RequestParam int page,
                                                                         @RequestParam int size) {
        Pageable pageable = setPagination(page, size);
        return new ResponseEntity<>(
                messageService.findByMicroEmprendimientoId(id, pageable),
                HttpStatus.OK);
    }

    /**
     * Guardar nuevos mensajes
     *
     * @return void
     */
    @PostMapping("/save")
    public ResponseEntity<Void> save(@Valid @RequestBody ContactMessageRequestSave request) {
        messageService.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Modificar Estado del mensaje entre "Gestionado" y "No gestionado"
     *
     * @return void
     */
    @PatchMapping("/admin/{id}/{status}")
    public ResponseEntity<Map<String, String>> changeStatus(@PathVariable int id, @PathVariable boolean status) throws MessagingException {
        return new ResponseEntity<>(messageService.changeStatus(id, status), HttpStatus.OK);
    }

    // ----------------------------------------------------

    // Configuración de paginación
    private Pageable setPagination(int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, "datetime"));
    }

}



