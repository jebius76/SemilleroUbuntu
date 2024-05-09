package com.semillero.ubuntu.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDto {
    private String title;
    private String description;
    private boolean deleted;
    private LocalDateTime creationDate;
    private int views;
    private List<String> url_images_publications;
    private List<MultipartFile> images;
}
