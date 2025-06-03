package com.libreria.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.libreria.DTOs.SolicitudAutenticacion;
import com.libreria.DTOs.RegisterDTO;
import com.libreria.enums.RolEnum;
import com.libreria.exceptions.MyException;
import com.libreria.services.AutenticacionService;
import com.libreria.services.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private AutenticacionService authService;

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin(ModelMap model) {
        // Agregar DTO vacío para el formulario
        model.addAttribute("loginRequest", new SolicitudAutenticacion());
        return "login.html";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
        return "redirect:/usuario/login";
    }

    @GetMapping("/register")
    public String registrar(ModelMap model) {
        model.addAttribute("registerRequest", new RegisterDTO());
        return "registro.html";
    }

    @PostMapping("/signup")
    public String registrado(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, ModelMap model) {

        RegisterDTO registerDTO = new RegisterDTO();

        try {

            registerDTO.setNombre(nombre);
            registerDTO.setEmail(email);
            registerDTO.setUsername(email);
            registerDTO.setPassword(password);
            registerDTO.setRol(RolEnum.USER);

            authService.registrar(registerDTO, password2);

            model.put("exito", "Usuario registrado correctamente!");

            return "redirect:/login?success";

        } catch (MyException ex) {

            model.addAttribute("error", ex.getMessage());
            return "registro";
        }
    }

    @GetMapping("/panel")
    public String mostrarPanel(ModelMap model, Principal principal) {
        return usuarioService.findByEmail(principal.getName())
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("nombreUsuario", usuario.getNombre());
                    model.addAttribute("rolUsuario", usuario.getRol().name());
                    return "panel.html";
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Usuario no encontrado: " + principal.getName());
                    return "error";
                });
    }

    @PostMapping("/become-admin")
    public String cambiarRol(Principal principal, @RequestParam("codigo") String codigo, ModelMap model)
            throws MyException {
        usuarioService.cambiarRol(principal.getName(), codigo);

        model.addAttribute("mensaje", "¡Ahora sos ADMIN!");

        return "panel.html";
    }

}
