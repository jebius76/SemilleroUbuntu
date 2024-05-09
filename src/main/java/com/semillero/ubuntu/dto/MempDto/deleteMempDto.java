package com.semillero.ubuntu.dto.MempDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class deleteMempDto {
    @NotBlank(message = "Username must not be null or blank")
    private String userName;
    @NotBlank(message = "Password must not be null or blank")
    private String password;
    @Email(message = "Email invalid")
    private String email;
}
