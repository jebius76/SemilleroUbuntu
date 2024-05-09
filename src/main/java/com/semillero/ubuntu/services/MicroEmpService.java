package com.semillero.ubuntu.services;

import com.semillero.ubuntu.dto.MicroEmpDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface MicroEmpService {
    @Transactional
    ResponseEntity<?> showAll();

    @Transactional
    ResponseEntity<?> getMicroEmpById(Long id);

    @Transactional
    @Query("SELECT e FROM MicroEmpEntity e WHERE e.nombre = :name")
    ResponseEntity<?> findByName(String name);

    @Transactional
    ResponseEntity<?> createMicroEmp(MicroEmpDto newMemp);
    @Transactional
    ResponseEntity<?> updateMicroEmp(MicroEmpDto editMemp, Long id);
    @Transactional
    ResponseEntity<?> deleteMicroEmp(Long id);
    @Transactional
    ResponseEntity<?> isActivoOrNot(Long id, boolean activo);


}
