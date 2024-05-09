package com.semillero.ubuntu.entities;

import com.semillero.ubuntu.initializer.RolesNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * User entity. Only stores username, password and role and is used to
 * login.
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;
    @ManyToOne
    private Role role;
}
