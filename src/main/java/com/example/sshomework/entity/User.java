package com.example.sshomework.entity;

import com.example.sshomework.dto.user.RolesUser;
import lombok.*;
import org.springframework.security.crypto.bcrypt.*;

import javax.persistence.*;

/**
 * @author Aleksey Romodin
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class User {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RolesUser role;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
