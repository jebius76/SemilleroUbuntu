package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.dto.ProvinceSimpleDto;
import com.semillero.ubuntu.entities.Province;
import com.semillero.ubuntu.services.IProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/provinces")
public class ProvinceController {

    private final IProvinceService provinceService;

    @GetMapping
    public ResponseEntity<List<Province>> findAll(){
        return new ResponseEntity<>(provinceService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{country}")
    public ResponseEntity<List<ProvinceSimpleDto>> findByCountryName(@PathVariable String country){
        return new ResponseEntity<>(provinceService.findByCountryName(country), HttpStatus.OK);
    }

}
