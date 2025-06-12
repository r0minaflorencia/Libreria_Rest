package com.libreria.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.libreria.DTOs.SolicitudAutenticacion;
import com.libreria.entities.Usuario;
import com.libreria.exceptions.MyException;
import com.libreria.DTOs.RegisterDTO;
import com.libreria.DTOs.RespuestaAutenticacion;

@Service
public class AutenticacionService {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    public RespuestaAutenticacion login(SolicitudAutenticacion solicitudAutenticacion) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                solicitudAutenticacion.getEmail(),
                solicitudAutenticacion.getPassword());

        authenticationManager.authenticate(authToken);

        Usuario usuario = usuarioService.buscarPorEmail(solicitudAutenticacion.getEmail());

        String jwt = jwtService.generarToken(usuario, generateExtraClaims(usuario));

        return new RespuestaAutenticacion(jwt);
    }

    public void registrar(RegisterDTO registerDTO, String password2) throws MyException {

        usuarioService.registrar(registerDTO.getNombre(), registerDTO.getEmail(), registerDTO.getPassword(), password2);

    }

    private Map<String, Object> generateExtraClaims(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", usuario.getNombre());
        extraClaims.put("rol", usuario.getRol());
        return extraClaims;
    }

}
