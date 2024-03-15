package com.semillero.ubuntu.mapper;

import com.semillero.ubuntu.dto.LoginUserResponseDto;
import com.semillero.ubuntu.entity.UserEntity;
import com.semillero.ubuntu.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class UserEntityMapper {

    UserEntityRepository userEntityRepository;
    public LoginUserResponseDto mapLoginUserResponseDto(String email){

        UserEntity userEntity = userEntityRepository.findByEmail(email).get();
        return new LoginUserResponseDto(userEntity.getId(), userEntity.getUserName(), userEntity.getEmail());
    }

}
