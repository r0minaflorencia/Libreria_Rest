package com.libreria.controllers;

import com.libreria.entities.Editorial;
import com.libreria.exceptions.MyException;
import com.libreria.services.EditorialService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialService service;

    @GetMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')")
    public String registrar() {
        return "editorial_form.html";
    }

    @PostMapping("/registro")
    @PreAuthorize("hasRole('ADMIN')")
    public String registro(@RequestParam String nombre, ModelMap model) {

        try {
            service.crear(nombre);
            model.put("exito", "Se registró con exito");
        } catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "editorial_form.html";
        }

        return "index.html";
    }

    @GetMapping("/lista")
    @PreAuthorize("permitAll()")
    public String listar(ModelMap modelo) {

        List<Editorial> editoriales = service.listarTodo();

        modelo.addAttribute("editoriales", editoriales);

        return "editorial_list.html";
    }

    @GetMapping("/info/{id}")
    @PreAuthorize("permitAll()")
    public String info(@PathVariable Long id, ModelMap modelo) {

        Editorial editorial = service.getOne(id);
        modelo.addAttribute("editorial", editorial);

        return "editorial_ficha.html";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, ModelMap modelo) {

        try {
            service.eliminar(id);
            modelo.put("exito", "Se eliminó correctamente la editorial.");

        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "editorial_list.html";
        }

        return "index.html";

    }

    @GetMapping("/modificar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String modificar(@PathVariable Long id, ModelMap modelo) {

        modelo.put("editorial", service.getOne(id));

        return "editorial_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String modificar(@PathVariable Long id, String nombre, ModelMap modelo) {
        try {

            service.modificar(id, nombre);

            return "redirect:../lista";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "editorial_modificar.html";
        }

    }

}
