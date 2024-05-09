package com.semillero.ubuntu.controllers;

import com.semillero.ubuntu.dto.MsgCloudinary;
import com.semillero.ubuntu.entities.ImagesEntity;
import com.semillero.ubuntu.services.CloudinaryService;
import com.semillero.ubuntu.services.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary")
@CrossOrigin
public class CloudinaryController {
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private ImagesService imagesService;

    @GetMapping("")
    public ResponseEntity<List<ImagesEntity>> list() {
        List<ImagesEntity> list = imagesService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMultiple(@RequestParam("multipartFile") List<MultipartFile> files) {
        try {
            List<ImagesEntity> imagesEntities = new ArrayList<>();
            for (MultipartFile file : files) {
                // Verifica si el tamaño del archivo excede los 3MB
                if (file.getSize() > 3 * 1024 * 1024) {
                    return new ResponseEntity(new MsgCloudinary("El tamaño del archivo excede los 5MB permitidos."), HttpStatus.BAD_REQUEST);
                }
                //Checkea que el file sea una imagen
                BufferedImage bi = ImageIO.read(file.getInputStream());
                if (bi == null) {
                    return new ResponseEntity(new MsgCloudinary("Tipo de archivo no válido."), HttpStatus.BAD_REQUEST);
                }

                //Sube la imagen a cloudinary
                Map result = cloudinaryService.upload(file);
                ImagesEntity images = new ImagesEntity((String) result.get("original_filename"), (String) result.get("url"), (String) result.get("public_id"));
                imagesEntities.add(images);

            }
            //Guarda la imagen en la db
            imagesService.saveAll(imagesEntities);
            return new ResponseEntity(new MsgCloudinary("Imágenes subidas correctamente."), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity(new MsgCloudinary("No se pudieron subir los archivos."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) throws IOException {
        if (!imagesService.exists(id)) {
            return new ResponseEntity(new MsgCloudinary("Not exists."), HttpStatus.NOT_FOUND);
        }
        ImagesEntity images = imagesService.getOne(id).get();
        Map result = cloudinaryService.delete(images.getImageId());
        imagesService.delete(id);
        return new ResponseEntity(new MsgCloudinary("Image was deleted"), HttpStatus.OK);
    }

}
