package com.semillero.ubuntu.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String imageUrl;
    private String imageId;

    public ImagesEntity() {
    }

    public ImagesEntity(String imageUrl, String name, String imageId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.imageId = imageId;
    }
}
