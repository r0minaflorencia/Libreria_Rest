package com.libreria.services;

import java.util.List;
import java.util.Optional;

import com.libreria.DTOs.RegisterDTO;
import com.libreria.DTOs.UsuarioDTO;
import com.libreria.entities.Usuario;
import com.libreria.exceptions.MyException;

public interface UsuarioService {

  // Crear un nuevo usuario
  UsuarioDTO save(Usuario usuario);

  // Buscar usuario por ID
  Optional<Usuario> findById(Long id);

  // Buscar todos los usuarios
  List<Usuario> findAll();

  // Buscar usuario por email
  Optional<Usuario> findByEmail(String email);

  Usuario buscarPorEmail(String email);

  // Buscar usuario por nombre de usuario
  Usuario findByUsername(String username);

  // Actualizar usuario
  Usuario update(Usuario usuario);

  // Eliminar usuario por ID
  void deleteById(Long id);

  // Verificar si existe un usuario por email
  boolean existsByEmail(String email);

  // Verificar si existe un usuario por nombre de usuario
  boolean existsByUsername(String username);

  // Validar credenciales de login
  Optional<Usuario> validateCredentials(String email, String password);

  // Cambiar contraseña
  boolean changePassword(Long userId, String oldPassword, String newPassword);

  // Activar/desactivar usuario
  Usuario toggleUserStatus(Long userId);

  // Convertir
  Usuario convertirDTOaEntity(RegisterDTO registroDTO);

  // Mi método para registrar
  void registrar(String nombre, String email, String password, String password2) throws MyException;

  // Mi método para cambiar rol
  void cambiarRol(String email, String codigo);

}
