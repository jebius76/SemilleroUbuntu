package com.semillero.ubuntu.mapper;

import com.semillero.ubuntu.dto.contactMessage.ContactMessageRequestSave;
import com.semillero.ubuntu.dto.contactMessage.ContactMessageResponse;
import com.semillero.ubuntu.dto.contactMessage.MicroemprendimientoResponse;
import com.semillero.ubuntu.entities.ContactMessage;
import com.semillero.ubuntu.entities.MicroEmpEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;


public class ContactMessageMapper {

    // Mapear de DTO a Entidad
    public static ContactMessage toEntity(ContactMessageRequestSave request) {

        MicroEmpEntity microemp = new MicroEmpEntity();
        microemp.setId((long) request.getMicroemprendimientoId());

        return ContactMessage.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .message(request.getMessage())
                .microemprendimiento(microemp)

                .build();
    }

     //Mapear de Entidad a DTO de respuesta
    public static ContactMessageResponse toResponse(ContactMessage contactMessage) {

        MicroemprendimientoResponse microResponse = MicroemprendimientoResponse.builder()
                .id(contactMessage.getMicroemprendimiento().getId())
                .name(contactMessage.getMicroemprendimiento().getNombre())
                .build();

        return ContactMessageResponse.builder()
                .id(contactMessage.getId())
                .fullName(contactMessage.getFullName())
                .email(contactMessage.getEmail())
                .phone(contactMessage.getPhone())
                .message(contactMessage.getMessage())
                .managed(contactMessage.isManaged())
                .datetime(contactMessage.getDatetime())
                .microemprendimiento(microResponse)

                .build();
    }

    //Mapear de Entidad (con paginación) a DTO de respuesta (con paginación)
    public static Page<ContactMessageResponse> toResponseList(Page<ContactMessage> list) {
        List<ContactMessageResponse> responseList = list.getContent().stream()
                .map(contactMessage -> {
                    MicroemprendimientoResponse microResponse = MicroemprendimientoResponse.builder()
                            .id(contactMessage.getMicroemprendimiento().getId())
                            .name(contactMessage.getMicroemprendimiento().getNombre())
                            .build();

                    return ContactMessageResponse.builder()
                            .id(contactMessage.getId())
                            .fullName(contactMessage.getFullName())
                            .email(contactMessage.getEmail())
                            .phone(contactMessage.getPhone())
                            .message(contactMessage.getMessage())
                            .managed(contactMessage.isManaged())
                            .datetime(contactMessage.getDatetime())
                            .microemprendimiento(microResponse)
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, list.getPageable(), list.getTotalElements());
    }


}
