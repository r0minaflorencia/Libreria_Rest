package com.libreria.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.libreria.DTOs.RegisterDTO;
import com.libreria.DTOs.UsuarioDTO;
import com.libreria.entities.Usuario;
import com.libreria.enums.RolEnum;
import com.libreria.exceptions.MyException;
import com.libreria.repositories.UsuarioRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.secret-code}")
    private String ADMIN_CODE;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return usuario; // Directamente retorna el Usuario ya que implementa UserDetails
    }

    public UserDetails loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail));

        return usuario;
    }

    @Override
    @Transactional
    public UsuarioDTO save(Usuario usuario) {
        Usuario guardado = usuarioRepo.save(usuario);
        return convertirADTO(guardado);
    }

    // Métodos de conversión DTO
    public UsuarioDTO convertirADTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    @Override
    @Transactional
    public void registrar(String nombre, String email, String password, String password2)
            throws MyException {

        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(RolEnum.USER);

        usuarioRepo.save(usuario);
    }

    private void validar(String nombre, String email, String password, String password2) throws MyException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new MyException("El nombre no puede estar vacío.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new MyException("El email no puede estar vacío.");
        }

        if (existsByEmail(email)) {
            throw new MyException("El email ya está registrado");
        }

        if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            throw new MyException("El email no tiene un formato válido.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new MyException("La contraseña no puede estar vacía.");
        }

        if (password.length() < 8) {
            throw new MyException("La contraseña debe tener al menos 8 caracteres.");
        }

        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas no coinciden.");
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")) {
            throw new MyException(
                    "La contraseña debe tener al menos una letra mayúscula, una minúscula, un número y un carácter especial.");
        }
    }

    @Transactional
    public void cambiarRol(String email, String codigo) {
        Optional<Usuario> optionalUsuario = findByEmail(email);

        if (optionalUsuario.isPresent() && codigo.equals(ADMIN_CODE)) {
            Usuario usuario = optionalUsuario.get();
            usuario.setRol(RolEnum.ADMIN); 
            usuarioRepo.save(usuario);
        } else {
            throw new EntityNotFoundException("Usuario: " + email + " no encontrado.");
        }
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepo.findById(id);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepo.findByEmail(email);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepo.findByUsername(username).get();
    }

    @Override
    @Transactional
    public Usuario update(Usuario usuario) {
        if (usuarioRepo.existsById(usuario.getId())) {
            // Si se está actualizando la contraseña, encriptarla
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            } else {
                // Si no se proporciona contraseña, mantener la existente
                Usuario usuarioExistente = usuarioRepo.findById(usuario.getId()).orElse(null);
                if (usuarioExistente != null) {
                    usuario.setPassword(usuarioExistente.getPassword());
                }
            }
            return usuarioRepo.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + usuario.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (usuarioRepo.existsById(id)) {
            usuarioRepo.deleteById(id);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepo.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioRepo.existsByUsername(username);
    }

    @Override
    public Optional<Usuario> validateCredentials(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verificar si la contraseña coincide usando el encoder
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(userId);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verificar que la contraseña antigua sea correcta
            if (passwordEncoder.matches(oldPassword, usuario.getPassword())) {
                // Encriptar y guardar la nueva contraseña
                usuario.setPassword(passwordEncoder.encode(newPassword));
                usuarioRepo.save(usuario);
                return true;
            }
        }

        return false;
    }

    @Override
    @Transactional
    public Usuario toggleUserStatus(Long userId) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(userId);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEnabled(!usuario.isEnabled()); // Invertir el estado
            return usuarioRepo.save(usuario);
        }

        throw new RuntimeException("Usuario no encontrado con ID: " + userId);
    }

    public List<Usuario> findByRol(String rol) {
        return usuarioRepo.findByRol(rol);
    }

    public List<Usuario> findByEnabled(boolean enabled) {
        return usuarioRepo.findByEnabled(enabled);
    }

    public Usuario convertirDTOaEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());

        return usuario;
    }

    @Override
    public Usuario convertirDTOaEntity(RegisterDTO registroDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setRol(RolEnum.USER);

        return usuario;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepo.buscarPorEmail(email);
    }

}
