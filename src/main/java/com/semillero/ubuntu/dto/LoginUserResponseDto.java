package com.semillero.ubuntu.dto;

import com.semillero.ubuntu.initializer.RolesNames;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponseDto {

    private Long id;
    private String userName;
    private String email;
    private String roles;
}
