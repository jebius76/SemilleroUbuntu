package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.dto.MicroEmpDto;
import com.semillero.ubuntu.entities.ImagesEntity;
import com.semillero.ubuntu.entities.MicroEmpEntity;
import com.semillero.ubuntu.entities.PublicationEntity;
import com.semillero.ubuntu.repositories.MicroEmpsRepository;
import com.semillero.ubuntu.services.CloudinaryService;
import com.semillero.ubuntu.services.MicroEmpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MicroEmpServiceImpl implements MicroEmpService {
    @Autowired
    private MicroEmpsRepository microEmpsRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<?> showAll() {
        try {
            List<?> microEmps = microEmpsRepository.findAll();
            return ResponseEntity.ok(microEmps);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public ResponseEntity<?> getMicroEmpById(Long id) {
        try {
            Optional<MicroEmpEntity> microEmpEntity = microEmpsRepository.findById(id);
            return ResponseEntity.ok(microEmpEntity);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> findByName(String name) {
        try {
            List<?> findMemps = microEmpsRepository.findByName(name);
            return ResponseEntity.ok(findMemps);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    public ResponseEntity<?> createMicroEmp(MicroEmpDto newMemp) {

        try {
            List<String> urlImages = new ArrayList<>();
            List<MultipartFile> allImages = newMemp.getImages();

            if (allImages.isEmpty()) {
                return ResponseEntity.badRequest().body("Subir al menos una imagen.");
            }

            if (allImages.size() > 3) {
                return ResponseEntity.badRequest().body("Solo subir hasta 3 imagenes.");
            }

            for (MultipartFile file : allImages) {
                // Verifica si el tamaño del archivo excede los 3MB
                if (file.getSize() > 3 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body("El tamaño del archivo excede los 5MB permitidos.");
                }
                // Checkea que el archivo sea una imagen
                BufferedImage bi = ImageIO.read(file.getInputStream());
                if (bi == null) {
                    return ResponseEntity.badRequest().body("Tipo de archivo no válido.");
                }
                // Sube la imagen a Cloudinary
                Map<String, String> result = cloudinaryService.upload(file);
                // Obtiene la URL de la imagen subida a Cloudinary
                String imageUrl = result.get("url");
                // Agrega la URL al array de URLs
                urlImages.add(imageUrl);
            }

            MicroEmpEntity microEmpEntity = new MicroEmpEntity();
            microEmpEntity.setNombre(newMemp.getNombre());
            microEmpEntity.setDescripcion(newMemp.getDescripcion());
            microEmpEntity.setMasInformacion(newMemp.getMasInformacion());
            microEmpEntity.setPais(newMemp.getPais());
            microEmpEntity.setEmail(newMemp.getEmail());
            microEmpEntity.setProvincia(newMemp.getProvincia());
            microEmpEntity.setCiudad(newMemp.getCiudad());
            microEmpEntity.setRubro(newMemp.getRubro());
            microEmpEntity.setSubrubro(newMemp.getSubrubro());
            microEmpEntity.setActivo(true);
            microEmpEntity.setGestionado(false);
            microEmpEntity.setFechaCreacion(LocalDateTime.now());
            microEmpEntity.setUrl_images(urlImages);

            microEmpsRepository.save(microEmpEntity);
            return ResponseEntity.ok().body("Microemprendimiento creado!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Faltan atributos o están mal cargados | " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateMicroEmp(MicroEmpDto editMemp, Long id) {
        try {
            Optional<MicroEmpEntity> response = microEmpsRepository.findById(id);
            if (response.isPresent()) {
                List<String> urlImages = new ArrayList<>();
                List<MultipartFile> allImages = editMemp.getImages() != null ? editMemp.getImages() : new ArrayList<>();
                MicroEmpEntity updateMicroEmp = response.get();
                int limitImages = editMemp.getUrl_images().size() + allImages.size();
                if (limitImages > 3) {
                    return ResponseEntity.badRequest().body("Solo hasta 3 imágenes.");
                }
                // Obtiene las URL de las imágenes existentes
                List<String> new_urls_images_dto = new ArrayList<>(editMemp.getUrl_images());
                List<String> existing_Url_Images = response.get().getUrl_images();

                // Elimina las URL de las imágenes existentes que no están en las nuevas imágenes
                existing_Url_Images.forEach(url -> {
                    if (!new_urls_images_dto.contains(url)) {
                        // Obtiene el ID de la imagen de Cloudinary a partir de la URL
                        String publicId = cloudinaryService.getPublicIdFromUrl(url);
                        // Elimina la imagen de Cloudinary usando el ID
                        try {
                            cloudinaryService.delete(publicId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Elimina las URL de las imágenes que ya no están en la lista de nuevas imágenes
                existing_Url_Images.retainAll(new_urls_images_dto);

                // Agrega las nuevas URL de imágenes
                if (allImages != null) {
                    for (MultipartFile file : allImages) {
                        // Verifica si el tamaño del archivo excede los 3MB
                        if (file.getSize() > 3 * 1024 * 1024) {
                            return ResponseEntity.badRequest().body("El tamaño del archivo excede los 5MB permitidos.");
                        }
                        // Checkea que el archivo sea una imagen
                        BufferedImage bi = ImageIO.read(file.getInputStream());
                        if (bi == null) {
                            return ResponseEntity.badRequest().body("Tipo de archivo no válido.");
                        }
                        // Sube la imagen a Cloudinary
                        Map<String, String> result = cloudinaryService.upload(file);
                        // Obtiene la URL de la imagen subida a Cloudinary
                        String imageUrl = result.get("url");
                        // Agrega la URL al array de URLs
                        urlImages.add(imageUrl);
                    }
                }

                // Agrega las nuevas URL de imágenes
                urlImages.addAll(new_urls_images_dto);

                updateMicroEmp.setNombre(editMemp.getNombre());
                updateMicroEmp.setDescripcion(editMemp.getDescripcion());
                updateMicroEmp.setMasInformacion(editMemp.getMasInformacion());
                updateMicroEmp.setPais(editMemp.getPais());
                updateMicroEmp.setEmail(editMemp.getEmail());
                updateMicroEmp.setProvincia(editMemp.getProvincia());
                updateMicroEmp.setCiudad(editMemp.getCiudad());
                updateMicroEmp.setRubro(editMemp.getRubro());
                updateMicroEmp.setSubrubro(editMemp.getSubrubro());
                updateMicroEmp.setActivo(editMemp.isActivo());
                updateMicroEmp.setGestionado(editMemp.isGestionado());
                updateMicroEmp.setUrl_images(urlImages);

                microEmpsRepository.save(updateMicroEmp);
                return ResponseEntity.ok().body("Microemprendimiento actualizado!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Faltan atributos o están mal cargados | " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("No se pudo encontrar o no existe!");
    }


    @Override
    public ResponseEntity<?> deleteMicroEmp(Long id) {
        try {
            Optional<MicroEmpEntity> response = microEmpsRepository.findById(id);
            if (response.isPresent()) {
                MicroEmpEntity delete_Memp = response.get();
                microEmpsRepository.delete(delete_Memp);
                return ResponseEntity.ok().body("Microemprendimiento eliminado!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se encontró o pudo eliminar " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("No se pudo encontrar o no existe!");
    }

    @Override
    public ResponseEntity<?> isActivoOrNot(Long id, boolean activo) {
        Optional<MicroEmpEntity> response = microEmpsRepository.findById(id);
        if (response.isPresent()) {
            MicroEmpEntity updateVisibleOnly = response.get();
            updateVisibleOnly.setActivo(activo);
            microEmpsRepository.save(updateVisibleOnly);
        }
        return null;
    }
}
