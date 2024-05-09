package com.semillero.ubuntu.dto;

import jakarta.persistence.Id;
import lombok.Data;
@Data
public class ImageDto {
    @Id
    public Long id;
    private String cloudinaryUrl;

}
