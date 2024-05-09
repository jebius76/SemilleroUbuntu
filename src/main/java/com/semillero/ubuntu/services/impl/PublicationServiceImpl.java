package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.dto.PublicationDto;
import com.semillero.ubuntu.entities.MicroEmpEntity;
import com.semillero.ubuntu.entities.PublicationEntity;
import com.semillero.ubuntu.repositories.PublicationRepository;
import com.semillero.ubuntu.services.CloudinaryService;
import com.semillero.ubuntu.services.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Optional;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Override
    public ResponseEntity<?> showAll() {
        try {
            List<?> publications = publicationRepository.findAll();
            return ResponseEntity.ok(publications);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> updateViews(Long id) {
        Optional<PublicationEntity> response = publicationRepository.findById(id);
        try {
            if (response.isPresent()) {
                PublicationEntity updateViewsPublic = response.get();
                int views = updateViewsPublic.getViews() + 1;
                updateViewsPublic.setViews(views);
                publicationRepository.save(updateViewsPublic);
            }
            return ResponseEntity.ok().body("Nueva Visita!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se sumo la nueva visita | " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> isActivoOrNot(Long id, boolean visible) {
        Optional<PublicationEntity> response = publicationRepository.findById(id);
        if (response.isPresent()) {
            PublicationEntity updateVisibleOnly = response.get();
            updateVisibleOnly.setVisible(visible);
            publicationRepository.save(updateVisibleOnly);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getPublicationById(Long id) {
        try {
            Optional<PublicationEntity> publications = publicationRepository.findById(id);
            return ResponseEntity.ok(publications);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> createPublication(PublicationDto publicationDto) {
        try {
            List<String> urlImages = new ArrayList<>();
            List<MultipartFile> allImages = publicationDto.getImages();
            if (allImages.size() > 3) {
                return ResponseEntity.badRequest().body("Solo hasta 3 imagenes.");
            }
            for (MultipartFile file : allImages) {
                // Verifica si el tamaño del archivo excede los 3MB
                if (file.getSize() > 3 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body("El tamaño del archivo excede los 3MB permitidos.");
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

            PublicationEntity new_publication = new PublicationEntity();
            new_publication.setTitle(publicationDto.getTitle());
            new_publication.setDescription(publicationDto.getDescription());
            new_publication.setUrl_images(urlImages);
            new_publication.setViews(0);
            new_publication.setVisible(true);
            new_publication.setCreationDate(LocalDateTime.now());

            publicationRepository.save(new_publication);
            return ResponseEntity.ok().body("Publicacion creada!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Faltan atributos o están mal cargados | " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updatePublication(PublicationDto publicationDto, Long id) {
        try {
            Optional<PublicationEntity> response = publicationRepository.findById(id);
            if (response.isPresent()) {
                List<String> urlImages = new ArrayList<>();
                List<MultipartFile> allImages = publicationDto.getImages() != null ? publicationDto.getImages() : new ArrayList<>();
                PublicationEntity updatePublication = response.get();
                int limitImages = publicationDto.getUrl_images_publications().size() + allImages.size();
                if (limitImages > 3) {
                    return ResponseEntity.badRequest().body("Solo hasta 3 imágenes.");
                }
                updatePublication.setTitle(publicationDto.getTitle());
                updatePublication.setDescription(publicationDto.getDescription());
                // Obtiene las URL de las imágenes existentes
                List<String> new_urls_images_dto = new ArrayList<>(publicationDto.getUrl_images_publications());
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
                new_urls_images_dto.retainAll(existing_Url_Images);
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
                new_urls_images_dto.addAll(urlImages);
                // Actualiza las URL de las imágenes en la entidad de publicación
                updatePublication.setUrl_images(new_urls_images_dto);
                if (updatePublication.getUrl_images().size() > 3) {
                    return ResponseEntity.badRequest().body("Solo hasta 3 imagenes.");
                }
                publicationRepository.save(updatePublication);
                return ResponseEntity.ok().body("Publicacion actualizada!");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Faltan atributos o están mal cargados | " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("No se pudo encontrar o no existe!");
    }
}


