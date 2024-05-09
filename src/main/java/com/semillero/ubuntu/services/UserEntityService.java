package com.semillero.ubuntu.services;

import com.semillero.ubuntu.dto.RegisterUserDto;
import com.semillero.ubuntu.entities.UserEntity;

import java.util.Optional;

public interface UserEntityService {

    UserEntity registerUser(RegisterUserDto registerUserDto);

    Optional<UserEntity> findByEmail(String email);
}
