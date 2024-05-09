package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.dto.ErrorMessageDto;
import com.semillero.ubuntu.dto.LoginUserDto;
import com.semillero.ubuntu.dto.RegisterUserDto;
import com.semillero.ubuntu.exceptions.UserAlreadyExistException;
import com.semillero.ubuntu.mapper.UserEntityMapper;
import com.semillero.ubuntu.security.CookieGenerator;
import com.semillero.ubuntu.services.impl.UserEntityServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;


/**
 * Register and authorization controller.
 */

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${home.page.redirect}")
    private String homePage;

    @Autowired
    UserEntityServiceImpl userEntityService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CookieGenerator cookieGenerator;
    @Autowired
    UserEntityMapper userEntityMapper;

    /**
     * Controller to register a new user
     * @param registerUserDto
     * @return
     */
    @PostMapping ("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto){

        try {
            return new ResponseEntity<>(userEntityService.registerUser(registerUserDto), HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(new ErrorMessageDto(e.getClass().getName(), e.getMessage()), HttpStatus.CONFLICT);
        } catch (RuntimeException e){
            return new ResponseEntity<>(new ErrorMessageDto(e.getClass().getName(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserDto loginUserDto, HttpServletRequest request, HttpServletResponse response) {
        String loginEmail = loginUserDto.getEmail();
        String loginPassword = loginUserDto.getPassword();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginEmail,
                            loginPassword
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginEmail);

            // Verificar si el usuario tiene el correo electrónico "juansgb316@gmail.com"
            if (loginEmail.equals("juansgb316@gmail.com")) {
                // Si es así, asignar manualmente el rol de administrador
                List<GrantedAuthority> updatedAuthorities = new ArrayList<>(userDetails.getAuthorities());
                updatedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                userDetails = new User(userDetails.getUsername(), userDetails.getPassword(), updatedAuthorities);
            }

            response.addCookie(cookieGenerator.generateCookie(loginEmail));
            return new ResponseEntity<>(userEntityMapper.mapLoginUserResponseDto(loginEmail), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ErrorMessageDto(e.getClass().getName(), e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/loggedIn")
    public RedirectView loggedIn(HttpServletResponse response){
        response.addCookie(cookieGenerator.generateCookie(SecurityContextHolder.getContext().getAuthentication().getName()));
        return new RedirectView(homePage);
    }

    @GetMapping("/forbidden")
    public ResponseEntity<?> forbidden(){
        return new ResponseEntity<>(new ErrorMessageDto("Forbidden access", null), HttpStatus.FORBIDDEN);
    }

    @GetMapping("/loggedOut")
    public ResponseEntity<?> logout(HttpServletResponse response){
        return new ResponseEntity<>(new ErrorMessageDto("Logged out", null), HttpStatus.OK);
    }

    @GetMapping("/loginFailure")
    public ResponseEntity<?> loginFailure(){
        return new ResponseEntity<>("Login Failure", HttpStatus.FORBIDDEN);
    }
}
