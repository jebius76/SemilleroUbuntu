package com.semillero.ubuntu.repository;

//import com.jebius.appmedica.entity.Role;
import com.semillero.ubuntu.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(String role);
}
