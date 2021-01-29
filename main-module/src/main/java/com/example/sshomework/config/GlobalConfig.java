package com.example.sshomework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

/**
 * @author Aleksey Romodin
 */
@Configuration
public class GlobalConfig {
    @Bean
    public Pbkdf2PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder("enPass", 1000, 128);
    }
}
