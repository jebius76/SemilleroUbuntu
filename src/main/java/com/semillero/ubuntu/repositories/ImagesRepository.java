package com.semillero.ubuntu.repositories;

import com.semillero.ubuntu.entities.ImagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<ImagesEntity, Integer> {
    List<ImagesEntity> findByOrderById();
}
