package com.semillero.ubuntu.dto;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MicroEmpDto {
    private String nombre;
    private String descripcion;
    private String masInformacion;
    private String email;
    private String pais;
    private String provincia;
    private String ciudad;
    private String rubro;
    private String subrubro;
    private boolean activo;
    private boolean gestionado;
    private List<String> url_images;
    private List<MultipartFile> images;
}
