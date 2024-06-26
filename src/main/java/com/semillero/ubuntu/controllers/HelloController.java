package com.semillero.ubuntu.controllers;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @GetMapping("")
    public String hello(){
        return "Hello, you are authenticated as: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
