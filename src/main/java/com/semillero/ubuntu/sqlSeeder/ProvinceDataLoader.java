package com.semillero.ubuntu.sqlSeeder;

import com.semillero.ubuntu.entities.Country;
import com.semillero.ubuntu.entities.Province;
import com.semillero.ubuntu.repositories.ICountryRepository;
import com.semillero.ubuntu.repositories.IProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProvinceDataLoader {

    private final IProvinceRepository repository;
    private final ICountryRepository countryRepository;

    public void loadData(){

        if (repository.count() == 0){

            Country argentina = countryRepository.findByName("Argentina").get();
            Country uruguay = countryRepository.findByName("Uruguay").get();
            Country paraguay = countryRepository.findByName("Paraguay").get();
            Country chile = countryRepository.findByName("Chile").get();

            Province prov1;
            Province prov2;
            Province prov3;
            Province prov4;
            List<Province> list;


            /** Provincias de Argentina */
            prov1 = new Province();
            prov2 = new Province();
            prov3 = new Province();
            prov4 = new Province();

            prov1.setName("Buenos Aires");
            prov2.setName("Córdoba");
            prov3.setName("Santa Fe");
            prov4.setName("Mendoza");

            prov1.setCountry(argentina);
            prov2.setCountry(argentina);
            prov3.setCountry(argentina);
            prov4.setCountry(argentina);

            list = Arrays.asList(prov1, prov2, prov3, prov4);
            repository.saveAll(list);


            /** Provincias de Uruguay */
            prov1 = new Province();
            prov2 = new Province();
            prov3 = new Province();
            prov4 = new Province();

            prov1.setName("Montevideo");
            prov2.setName("Artigas");
            prov3.setName("Maldonado");
            prov4.setName("Florida");

            prov1.setCountry(uruguay);
            prov2.setCountry(uruguay);
            prov3.setCountry(uruguay);
            prov4.setCountry(uruguay);

            list = Arrays.asList(prov1, prov2, prov3, prov4);
            repository.saveAll(list);


            /** Provincias de Paraguay */
            prov1 = new Province();
            prov2 = new Province();
            prov3 = new Province();
            prov4 = new Province();

            prov1.setName("Asunción");
            prov2.setName("Concepción");
            prov3.setName("Alto Paraná");
            prov4.setName("San Pedro");

            prov1.setCountry(paraguay);
            prov2.setCountry(paraguay);
            prov3.setCountry(paraguay);
            prov4.setCountry(paraguay);

            list = Arrays.asList(prov1, prov2, prov3, prov4);
            repository.saveAll(list);


            /** Provincias de Chile */
            prov1 = new Province();
            prov2 = new Province();
            prov3 = new Province();
            prov4 = new Province();

            prov1.setName("Santiago de Chile");
            prov2.setName("Valparaíso");
            prov3.setName("Valdivia");
            prov4.setName("Coquimbo");

            prov1.setCountry(chile);
            prov2.setCountry(chile);
            prov3.setCountry(chile);
            prov4.setCountry(chile);

            list = Arrays.asList(prov1, prov2, prov3, prov4);
            repository.saveAll(list);

        }

    }

}
