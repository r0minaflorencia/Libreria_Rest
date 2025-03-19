package com.libreria.controllers;

import com.libreria.entities.Autor;
import com.libreria.exceptions.MyException;
import com.libreria.services.AutorService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Autor> autores = service.listarTodo();

        modelo.addAttribute("autores", autores);

        return "autor_list.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, ModelMap modelo) {
        try {

            service.eliminar(id);
            modelo.put("exito", "Se eliminó correctamente el autor seleccionado.");

        } catch (Exception ex) {

            modelo.put("error", ex.getMessage());
            return "autor_list.html";
        }

        return "index.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {

        modelo.put("autor", service.getOne(id));

        return "autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, String nombre, ModelMap modelo) {
        try {

            service.modificar(id, nombre);

            return "redirect:../lista";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "autor_modificar.html";
        }

    }

}
