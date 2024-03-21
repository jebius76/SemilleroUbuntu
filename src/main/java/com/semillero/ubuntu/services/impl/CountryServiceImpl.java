package com.semillero.ubuntu.services.impl;

import com.semillero.ubuntu.entities.Country;
import com.semillero.ubuntu.exceptions.CountryNotFound;
import com.semillero.ubuntu.repositories.ICountryRepository;
import com.semillero.ubuntu.services.ICountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements ICountryService {

    private final ICountryRepository countryRepository;


    private static final String EMPTY_LIST = "La lista está vacía, no se encontraron países";

    @Override
    public List<Country> findAll() {

        List<Country> countries = countryRepository.findAll();

        if (countries.isEmpty())
            throw new CountryNotFound(EMPTY_LIST);

        return countries;
    }
}
