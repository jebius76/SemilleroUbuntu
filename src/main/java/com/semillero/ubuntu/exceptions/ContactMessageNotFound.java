package com.semillero.ubuntu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactMessageNotFound extends RuntimeException{
    public ContactMessageNotFound(String message) {
        super(message);
    }
}
