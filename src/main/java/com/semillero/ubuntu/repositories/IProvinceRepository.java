package com.semillero.ubuntu.repositories;

import com.semillero.ubuntu.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Integer> {

    List<Province> findByCountryName(String countryName);
}
