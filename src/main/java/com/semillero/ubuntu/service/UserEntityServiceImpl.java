package com.semillero.ubuntu.service;

import com.semillero.ubuntu.dto.RegisterUserDto;
import com.semillero.ubuntu.entity.UserEntity;
import com.semillero.ubuntu.exception.UserAlreadyExistException;
import com.semillero.ubuntu.repository.RoleRepository;
import com.semillero.ubuntu.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserEntityServiceImpl implements UserEntityService{
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
        userEntity.setRole(roleRepository.findByRole("USER").get());
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
