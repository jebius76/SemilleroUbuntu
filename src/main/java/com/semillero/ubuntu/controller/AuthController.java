package com.semillero.ubuntu.controller;

import com.semillero.ubuntu.dto.ErrorMessageDto;
import com.semillero.ubuntu.dto.LoginUserDto;
import com.semillero.ubuntu.dto.RegisterUserDto;
import com.semillero.ubuntu.exception.UserAlreadyExistException;
import com.semillero.ubuntu.mapper.UserEntityMapper;
import com.semillero.ubuntu.security.CookieGenerator;
import com.semillero.ubuntu.security.JwtUtil;
import com.semillero.ubuntu.service.UserEntityServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


/**
 * Register and authorization controller.
 */

@RequiredArgsConstructor
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
            response.addCookie(cookieGenerator.generateCookie(loginEmail));
            return new ResponseEntity<>(userEntityMapper.mapLoginUserResponseDto(loginEmail), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ErrorMessageDto(e.getClass().getName(),e.getMessage()),HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/loggedIn")
    public RedirectView loggedIn(HttpServletResponse response){
        response.addCookie(cookieGenerator.generateCookie(SecurityContextHolder.getContext().getAuthentication().getName()));
        return new RedirectView(homePage);
    }

    @GetMapping("/forbidden")
    public ResponseEntity<?> forbidden(){
        return new ResponseEntity<>(new ErrorMessageDto("Forbidden access",null),HttpStatus.FORBIDDEN);
    }

    @GetMapping("/loggedOut")
    public ResponseEntity<?> logout(HttpServletResponse response){
        return new ResponseEntity<>(new ErrorMessageDto("Logged out",null),HttpStatus.OK);
    }

    @GetMapping("/loginFailure")
    public ResponseEntity<?> loginFailure(){
        return new ResponseEntity<>("Login Failure",HttpStatus.FORBIDDEN);
    }
}
