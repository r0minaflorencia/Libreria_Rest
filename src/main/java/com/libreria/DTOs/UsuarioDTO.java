package com.libreria.DTOs;

import lombok.Builder;

import com.libreria.enums.RolEnum;

@Builder
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String email;
    private String username;
    private RolEnum rol;

    // Constructores
    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nombre, String email, String username, RolEnum rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RolEnum getRol() {
        return rol;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }

}
