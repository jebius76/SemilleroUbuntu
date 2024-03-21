package com.semillero.ubuntu.services;

import com.semillero.ubuntu.dto.ProvinceSimpleDto;
import com.semillero.ubuntu.entities.Province;

import java.util.List;

public interface IProvinceService {

    List<Province> findAll();

    List<ProvinceSimpleDto> findByCountryName(String countryName);

}
