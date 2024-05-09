package com.semillero.ubuntu.dto.contactMessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ContactMessageResponse {

    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String message;
    private boolean managed;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datetime;
    private MicroemprendimientoResponse microemprendimiento;

}
