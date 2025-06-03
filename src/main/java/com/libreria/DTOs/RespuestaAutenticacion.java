package com.libreria.DTOs;

import lombok.Builder;

@Builder
public class RespuestaAutenticacion {

    private String token;
    private Long id;
    private String nombre;
    private String email;
    private String username;
    private String rol;

    // Constructor principal con todos los campos necesarios
    public RespuestaAutenticacion(String token, Long id, String nombre, String email, String username, String rol) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.rol = rol;
    }

    // Constructor simplificado solo con token y email
    public RespuestaAutenticacion(String token, String email, String rol) {
        this.token = token;
        this.email = email;
        this.rol = rol;
    }

    // Constructor para casos de error (token nulo)
    public RespuestaAutenticacion(String token) {
        this.token = token;
    }

    // Constructor vacío para deserialización
    public RespuestaAutenticacion() {
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public static RespuestaAutenticacion exito(String token, String email, String rol) {
        return RespuestaAutenticacion.builder()
                .token(token)
                .email(email)
                .rol(rol)
                .build();
    }

    @Override
    public String toString() {
        return "RespuestaAutenticacion{" +
                "token='" + (token != null ? "[TOKEN_PRESENT]" : "null") + '\'' +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
