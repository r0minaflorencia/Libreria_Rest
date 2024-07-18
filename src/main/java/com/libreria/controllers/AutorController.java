package com.libreria.controllers;

import com.libreria.exceptions.MyException;
import com.libreria.services.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorService service;

    @GetMapping("/registrar")
    public String registrar() {
        
        return "autor_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap model) {

        try {
            service.crear(nombre);
            model.put("exito", "Se registró con exito");
        } catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "autor_form.html";
        }
        return "index.html";

    }

}
