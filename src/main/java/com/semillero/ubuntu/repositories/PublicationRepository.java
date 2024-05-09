package com.semillero.ubuntu.repositories;

import com.semillero.ubuntu.entities.PublicationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Long> {


    @Query("SELECT p FROM PublicationEntity p WHERE p.creationDate BETWEEN :startDate AND :endDate")
    List<PublicationEntity> findByCreationDateBetween(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Transactional
    @Modifying
    @Query("UPDATE PublicationEntity p SET p.visible = :status WHERE p.id = :publicationId")
    void changeStatus(@Param("publicationId") Long publicationId,
                      @Param("status") boolean status);


}