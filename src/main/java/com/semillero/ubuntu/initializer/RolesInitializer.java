package com.semillero.ubuntu.initializer;

import com.semillero.ubuntu.entity.Role;
import com.semillero.ubuntu.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolesInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initializeData(){
        for (RolesNames role: RolesNames.values()){
            if (roleRepository.findByRole(role.name()).isEmpty()){
                roleRepository.save(new Role(null, role.name()));
            }
        }
    }
}
