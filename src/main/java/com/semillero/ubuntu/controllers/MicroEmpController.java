package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.dto.MicroEmpDto;
import com.semillero.ubuntu.services.MicroEmpService;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class MicroEmpController {

    @Autowired
    private MicroEmpService microEmpService;

    @GetMapping("/memps")
    public ResponseEntity<?> getAllMicroEmps() {
        try {
            return new ResponseEntity<>(microEmpService.showAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/memps/{id}")
    public ResponseEntity<?> getMicroEmpbyId(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(microEmpService.getMicroEmpById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/memps/changestatus/{id}")
    public ResponseEntity<?> updateViews(@PathVariable Long id, @RequestParam boolean activo) {
        try {
            return microEmpService.isActivoOrNot(id, activo);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/admin/memps/create")
    public ResponseEntity<?> createMicroEmp(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String masInformacion,
            @RequestParam String email,
            @RequestParam String pais,
            @RequestParam String provincia,
            @RequestParam String ciudad,
            @RequestParam String rubro,
            @RequestParam String subrubro,
            @RequestParam(required = false) boolean activo,
            @RequestParam(required = false) boolean gestionado,
            @RequestParam List<MultipartFile> images
    ) {
        try {
            MicroEmpDto microEmpDto = new MicroEmpDto();
            System.out.println("DTO received.");
            microEmpDto.setNombre(nombre);
            microEmpDto.setDescripcion(descripcion);
            microEmpDto.setMasInformacion(masInformacion);
            microEmpDto.setEmail(email);
            microEmpDto.setPais(pais);
            microEmpDto.setProvincia(provincia);
            microEmpDto.setCiudad(ciudad);
            microEmpDto.setRubro(rubro);
            microEmpDto.setSubrubro(subrubro);
            microEmpDto.setActivo(activo);
            microEmpDto.setGestionado(gestionado);
            microEmpDto.setImages(images);
            System.out.println("Creating MicroEmp...");
            return microEmpService.createMicroEmp(microEmpDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/admin/memps/edit/{id}")
    public ResponseEntity<?> editMicroEmp(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String masInformacion,
            @RequestParam String email,
            @RequestParam String pais,
            @RequestParam String provincia,
            @RequestParam String ciudad,
            @RequestParam String rubro,
            @RequestParam String subrubro,
            @RequestParam boolean activo,
            @RequestParam boolean gestionado,
            @RequestParam(required = false) List<MultipartFile> images,
            @RequestParam("url_images") List<String> url_images,
            @PathVariable Long id
    ) {
        try {

            MicroEmpDto memp = new MicroEmpDto();
            memp.setNombre(nombre);
            memp.setDescripcion(descripcion);
            memp.setMasInformacion(masInformacion);
            memp.setEmail(email);
            memp.setPais(pais);
            memp.setProvincia(provincia);
            memp.setCiudad(ciudad);
            memp.setRubro(rubro);
            memp.setSubrubro(subrubro);
            memp.setActivo(activo);
            memp.setGestionado(gestionado);
            memp.setImages(images);
            memp.setUrl_images(url_images);
            System.out.println("Actualizando MicroEmp");
            return microEmpService.updateMicroEmp(memp, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/admin/memps/delete/{id}")
    public ResponseEntity<?> deleteMicroEmp(@PathVariable Long id) {
        try {
            return microEmpService.deleteMicroEmp(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
}
