package com.semillero.ubuntu.service;

import com.semillero.ubuntu.config.PasswordEncoderConfig;
import com.semillero.ubuntu.entity.UserEntity;
import com.semillero.ubuntu.security.UserPrincipal;
import com.semillero.ubuntu.repository.RoleRepository;
import com.semillero.ubuntu.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private UserEntityRepository userEntityRepository;

    private RoleRepository roleRepository;

    private PasswordEncoderConfig passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User){
        String oAuth2UserEmail = oAuth2User.getAttributes().get("email").toString();

        if(userEntityRepository.findByEmail(oAuth2UserEmail).isEmpty()){
            registerNewUser(oAuth2UserEmail);
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(roleRepository.findByRole("USER").get());
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setName(oAuth2UserEmail);
        userPrincipal.setAuthorities(authorities);
        userPrincipal.setAttributes(oAuth2User.getAttributes());
        return userPrincipal;
    }

    private UserEntity registerNewUser(String oAuth2UserEmail){
        UserEntity user = new UserEntity();
        user.setUserName(oAuth2UserEmail);
        user.setEmail(oAuth2UserEmail);
        user.setPassword(passwordEncoder.passwordEncoder().encode("Secure!1976"));
        user.setRole(roleRepository.findByRole("USER").get());
        return userEntityRepository.save(user);
    }

}
