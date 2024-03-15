package com.semillero.ubuntu.service;

import com.semillero.ubuntu.dto.RegisterUserDto;
import com.semillero.ubuntu.entity.UserEntity;

import java.util.Optional;

public interface UserEntityService {

    UserEntity registerUser(RegisterUserDto registerUserDto);

    Optional<UserEntity> findByEmail(String email);
}
