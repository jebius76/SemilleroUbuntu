package com.semillero.ubuntu.repositories;

import com.semillero.ubuntu.entities.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface IContactMessageRepository extends JpaRepository<ContactMessage, Integer> {

    Page<ContactMessage> findByManagedIsTrue(Pageable pageable);

    Page<ContactMessage> findByManagedIsFalse(Pageable pageable);

    Page<ContactMessage> findByMicroemprendimientoId(int id, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE ContactMessage c SET c.managed = :status WHERE c.id = :contactMessageId")
    void changeStatus(@Param("contactMessageId") int contactMessageId,
                      @Param("status") boolean status);

    @Query("SELECT c.email FROM ContactMessage c")
    List<String> findAllEmails();

    Integer countByManagedIsTrue();
    Integer countByManagedIsFalse();

}
