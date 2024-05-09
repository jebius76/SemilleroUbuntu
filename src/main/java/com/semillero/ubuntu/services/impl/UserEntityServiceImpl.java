package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.dto.RegisterUserDto;
import com.semillero.ubuntu.entities.UserEntity;
import com.semillero.ubuntu.exceptions.UserAlreadyExistException;
import com.semillero.ubuntu.repositories.RoleRepository;
import com.semillero.ubuntu.repositories.UserEntityRepository;
import com.semillero.ubuntu.services.UserEntityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserEntityServiceImpl implements UserEntityService {
    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity registerUser(RegisterUserDto registerUserDto) {

        if (userEntityRepository.findByEmail(registerUserDto.getEmail()).isPresent()){
            throw new UserAlreadyExistException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(registerUserDto.getUserName());
        userEntity.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        if (registerUserDto.getEmail().equals("juansgb316@gmail.com")) {
            userEntity.setRole(roleRepository.findByRole("ADMIN").orElseThrow(() -> new RuntimeException("Role not found")));
        } else {
            userEntity.setRole(roleRepository.findByRole("USER").orElseThrow(() -> new RuntimeException("Role not found")));
        }
        userEntity.setEmail(registerUserDto.getEmail());
        try {
            return userEntityRepository.save(userEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error registering user");
        }
    }

    public Optional<UserEntity> findByEmail(String email){
        return userEntityRepository.findByEmail(email);
    }
}
