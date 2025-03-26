package com.libreria.controllers;

import com.libreria.entities.Autor;
import com.libreria.entities.Editorial;
import com.libreria.entities.Libro;
import com.libreria.exceptions.MyException;
import com.libreria.services.AutorService;
import com.libreria.services.EditorialService;
import com.libreria.services.LibroService;
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
@RequestMapping("/libro")
public class LibroController {

    // autocableado, inyección de dependencias, instancias únicas.
    @Autowired
    private LibroService service;
    @Autowired
    private AutorService autorService;
    @Autowired
    private EditorialService editorialService;

    @GetMapping("/registrar")
    public String registrar(ModelMap model) {

        List<Autor> autores = autorService.listarTodo();
        List<Editorial> editoriales = editorialService.listarTodo();

        model.addAttribute("autores", autores);
        model.addAttribute("editoriales", editoriales);

        return "libro_form.html";
    }

    /**
     * *
     * En mi método de tipo PostMapping agrego el objeto ModelMap, el cual me
     * sirve para insertar en él la información que voy a mostrar por pantalla o
     * que utilizaré en la interfaz del usuario (UI); en este caso lo utilizo
     * para mostrar el mensaje de error al usuario. Tambien, utilizo el
     * (required = false) para que los valores nulos pasen en su defecto a las
     * excepciones, de no ser así no entrarían siquiera al método. Se retorna
     * index.html si es satisfactorio.
     *
     * @param isbn
     * @param titulo
     * @param ejemplares
     * @param idAutor
     * @param idEditorial
     * @param model
     * @return index is returned if satisfactory
     */
    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) Long idAutor,
            @RequestParam(required = false) Long idEditorial, ModelMap model) {

        try {

            service.crear(isbn, titulo, ejemplares, idAutor, idEditorial);
            model.put("exito", "Se registró correctamente.");

        } catch (MyException ex) {
            List<Autor> autores = autorService.listarTodo();
            List<Editorial> editoriales = editorialService.listarTodo();

            model.addAttribute("autores", autores);
            model.addAttribute("editoriales", editoriales);

            model.put("error", ex.getMessage());

            return "libro_form.html"; // si hay algun error volver a cargar el formulario.
        }

        // si todo sale bien recargamos el index.
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Libro> libros = service.listarTodo();
        modelo.addAttribute("libros", libros);

        return "libro_list.html";
    }

    @GetMapping("/info/{isbn}")
    public String info(@PathVariable Long isbn, ModelMap modelo) {

        Libro libro = service.getOne(isbn);
        modelo.addAttribute("libro", libro);

        return "libro_ficha.html";
    }

    @GetMapping("/eliminar/{isbn}")
    public String eliminarLibro(@PathVariable Long isbn, ModelMap modelo) {
        try {
            service.eliminar(isbn);
            modelo.put("exito", "El libro se eliminó correctamente.");
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "libro_list.html";
        }

        return "redirect:../lista";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        modelo.put("libro", service.getOne(isbn));

        List<Autor> autores = autorService.listarTodo();
        List<Editorial> editoriales = editorialService.listarTodo();

        modelo.addAttribute("idAutorSelected", service.getOne(isbn).getAutor().getId());
        modelo.addAttribute("idEditorialSelected", service.getOne(isbn).getEditorial().getId());

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_modificar.html";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String descripcion,
            Long idAutor, Long idEditorial,
            ModelMap modelo) {
        try {

            service.modificar(isbn, titulo, ejemplares, descripcion, idAutor, idEditorial);
            modelo.put("exito", "se modificó correctamente");
            return "redirect:../lista";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "libro_modificar.html";
        }

    }

}
