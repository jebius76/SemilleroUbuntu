package com.semillero.ubuntu.mapper;

import com.semillero.ubuntu.dto.ProvinceSimpleDto;
import com.semillero.ubuntu.entities.Province;

public class ProvinceMapper {

    public static ProvinceSimpleDto entityToDto(Province province){
        ProvinceSimpleDto dto = new ProvinceSimpleDto();
        dto.setId(province.getId());
        dto.setName(province.getName());

        return dto;
    }

}
