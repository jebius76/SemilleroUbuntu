package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.dto.PublicationDto;
import com.semillero.ubuntu.services.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @GetMapping("/publications")
    public ResponseEntity<?> getAllPublications() {
        return new ResponseEntity<>(publicationService.showAll(), HttpStatus.OK);
    }

    @GetMapping("/publications/{id}")
    public ResponseEntity<?> getPublicationById(@PathVariable Long id) {
        try {
            return publicationService.getPublicationById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/publications/{id}")
    public ResponseEntity<?> updateViews(@PathVariable Long id) {
        try {
            return publicationService.updateViews(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/admin/publications/changestatus/{id}")
    public ResponseEntity<?> updateViews(@PathVariable Long id, @RequestParam boolean visible) {
        try {
            return publicationService.isActivoOrNot(id, visible);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    //create publication ADMIN
    @PostMapping("/admin/publications/create")
    public ResponseEntity<?> createPublication
    (@RequestParam List<MultipartFile> images,
     @RequestParam("title") String title,
     @RequestParam("description") String description) {
        System.out.println(title);
        System.out.println(description);
        System.out.println(images);
        try {
            PublicationDto publicationDto = new PublicationDto();
            publicationDto.setTitle(title);
            publicationDto.setImages(images);
            publicationDto.setDescription(description);
            return publicationService.createPublication(publicationDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/publications/edit/{id}")
    public ResponseEntity<?> editPublication(
            @PathVariable Long id,
            @RequestParam(required = false) List<MultipartFile> images,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("url_images") List<String> urlImages) {
        System.out.println(title);
        System.out.println(description);
        System.out.println(images);
        System.out.println(urlImages);
        try {
            PublicationDto publicationDto = new PublicationDto();
            publicationDto.setTitle(title);
            publicationDto.setImages(images);
            publicationDto.setDescription(description);
            publicationDto.setUrl_images_publications(urlImages);
            return publicationService.updatePublication(publicationDto, id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

}


