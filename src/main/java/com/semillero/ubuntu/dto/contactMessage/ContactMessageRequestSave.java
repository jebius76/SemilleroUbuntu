package com.semillero.ubuntu.dto.contactMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactMessageRequestSave {


    @NotBlank(message = "El nombre no debe estar vacío")
    @Size(min = 3, max = 60)
    private String fullName;

    @Email(message = "Email inválido")
    @NotBlank
    private String email;

    @NotBlank(message = "Número de teléfono inválido")
    private String phone;

    @Size(min = 3, max = 300)
    @NotBlank(message = "El mensaje debe tener entre 3 y 300 caracteres")
    private String message;

    @NotNull(message = "No asignaste un id de microemprendimiento")
    private int microemprendimientoId;

}
