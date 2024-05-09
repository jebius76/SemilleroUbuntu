package com.semillero.ubuntu.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class PublicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private int views;

    @ElementCollection
    @CollectionTable(name = "url_images_publications")
    @Column(name = "url")
    private List<String> url_images;

}
