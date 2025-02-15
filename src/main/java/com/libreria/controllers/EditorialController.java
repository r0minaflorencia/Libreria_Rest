package com.libreria.controllers;

import com.libreria.entities.Editorial;
import com.libreria.exceptions.MyException;
import com.libreria.services.EditorialService;

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
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialService service;

    @GetMapping("/registrar")
    public String registrar() {
        return "editorial/editorial_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap model) {

        try {
            service.crear(nombre);
            model.put("exito", "Se registró con exito");
        } catch (MyException ex) {
            model.put("error", ex.getMessage());
            return "editorial/editorial_form.html";
        }

        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Editorial> editoriales = service.listarTodo();

        modelo.addAttribute("editoriales", editoriales);

        return "editorial/editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {
        modelo.put("editorial", service.getOne(id));

        return "editorial/editorial_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, String nombre, ModelMap modelo) {
        try {
            service.modificar(id, nombre);

            return "redirect:../lista";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "editorial/editorial_modificar.html";
        }

    }


}
