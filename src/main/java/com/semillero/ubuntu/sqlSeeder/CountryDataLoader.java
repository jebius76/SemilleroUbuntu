package com.semillero.ubuntu.sqlSeeder;

import com.semillero.ubuntu.entities.Country;
import com.semillero.ubuntu.repositories.ICountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CountryDataLoader{

    private final ICountryRepository repository;

    public void loadData(){

        if (repository.count() == 0){

            Country country1 = new Country();
            Country country2 = new Country();
            Country country3 = new Country();
            Country country4 = new Country();

            country1.setName("Argentina");
            country2.setName("Uruguay");
            country3.setName("Paraguay");
            country4.setName("Chile");

            List<Country> list = Arrays.asList(country1, country2, country3, country4);

            repository.saveAll(list);

        }

    }

}
