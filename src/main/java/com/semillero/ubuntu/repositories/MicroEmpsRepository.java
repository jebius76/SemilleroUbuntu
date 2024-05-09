package com.semillero.ubuntu.repositories;

import com.semillero.ubuntu.entities.MicroEmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MicroEmpsRepository extends JpaRepository<MicroEmpEntity, Long> {
    @Query("SELECT e FROM MicroEmpEntity e WHERE e.nombre = :name")
    List<MicroEmpEntity> findByName(String name);

    @Query("SELECT m FROM MicroEmpEntity m WHERE m.fechaCreacion BETWEEN :startDate AND :endDate")
    List<MicroEmpEntity> findByCreationDateBetween(@Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);
}
