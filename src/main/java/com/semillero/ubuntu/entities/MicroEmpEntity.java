package com.semillero.ubuntu.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class MicroEmpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 100)
    private String email;

    @Column(length = 1000)
    private String masInformacion;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String provincia;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String rubro;

    @Column
    private String subrubro;

    @Column(nullable = false)
    private boolean activo;

    @Column(nullable = false)
    private boolean gestionado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ElementCollection
    @CollectionTable(name = "url_images_microEmp")
    @Column(name = "url")
    private List<String> url_images;


}
