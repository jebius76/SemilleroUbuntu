package com.semillero.ubuntu.services;

import com.semillero.ubuntu.entities.Country;

import java.util.List;

public interface ICountryService {

    List<Country> findAll();

}
