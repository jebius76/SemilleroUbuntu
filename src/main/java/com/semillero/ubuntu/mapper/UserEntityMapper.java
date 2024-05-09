package com.semillero.ubuntu.mapper;

import com.semillero.ubuntu.dto.LoginUserResponseDto;
import com.semillero.ubuntu.entities.UserEntity;
import com.semillero.ubuntu.repositories.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class UserEntityMapper {

    UserEntityRepository userEntityRepository;
    public LoginUserResponseDto mapLoginUserResponseDto(String email){

        UserEntity userEntity = userEntityRepository.findByEmail(email).get();
        return new LoginUserResponseDto(userEntity.getId(), userEntity.getUserName(), userEntity.getEmail(), userEntity.getRole().getRole());
    }

}
