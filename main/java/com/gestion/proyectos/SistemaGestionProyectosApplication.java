package com.gestion.proyectos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SistemaGestionProyectosApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaGestionProyectosApplication.class, args);
    }
}
