package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.dto.ProvinceSimpleDto;
import com.semillero.ubuntu.entities.Province;
import com.semillero.ubuntu.exceptions.ProvinceNotFound;
import com.semillero.ubuntu.mapper.ProvinceMapper;
import com.semillero.ubuntu.repositories.IProvinceRepository;
import com.semillero.ubuntu.services.IProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceServiceImpl implements IProvinceService {

    private final IProvinceRepository provinceRepository;


    private static final String EMPTY_LIST = "La lista está vacía, no se encontraron provincias";
    private static final String PROVINCES_NOT_FOUND = "No se encontraron provincias para el país seleccionado";


    @Override
    public List<Province> findAll() {
        List<Province> provinces = provinceRepository.findAll();

        if (provinces.isEmpty())
            throw new ProvinceNotFound(EMPTY_LIST);

        return provinces;
    }

    @Override
    public List<ProvinceSimpleDto> findByCountryName(String countryName) {

        List<Province> provinces = provinceRepository.findByCountryName(countryName);

        if (provinces.isEmpty())
            throw new ProvinceNotFound(PROVINCES_NOT_FOUND);

        return provinces.stream()
                .map(ProvinceMapper::entityToDto)
                .toList();
    }
}
