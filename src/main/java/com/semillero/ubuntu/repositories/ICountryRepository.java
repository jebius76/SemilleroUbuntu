package com.semillero.ubuntu.repositories;

import com.semillero.ubuntu.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ICountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findByName(String name);

}
