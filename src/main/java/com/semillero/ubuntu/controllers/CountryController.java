package com.semillero.ubuntu.controllers;


import com.semillero.ubuntu.entities.Country;
import com.semillero.ubuntu.services.ICountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

    private final ICountryService countryService;

    @GetMapping
    public ResponseEntity<List<Country>> findAll(){
        return new ResponseEntity<>(countryService.findAll(), HttpStatus.OK);
    }

}
