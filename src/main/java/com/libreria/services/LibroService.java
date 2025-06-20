package com.libreria.services;

import com.libreria.entities.Autor;
import com.libreria.entities.Editorial;
import com.libreria.entities.Libro;
import com.libreria.exceptions.MyException;
import com.libreria.repositories.AutorRepo;
import com.libreria.repositories.EditorialRepo;
import com.libreria.repositories.LibroRepo;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroService {

    // inyección de dependencias:
    @Autowired
    private LibroRepo libroRepo;
    @Autowired
    private AutorRepo autorRepo;
    @Autowired
    private EditorialRepo editorialRepo;

    // método para crear libro con atributos básicos:
    @Transactional
    public void crear(Long isbn, String titulo, String descripcion, Integer ejemplares,
            Long idAutor, Long idEditorial) throws MyException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        // instancio objetos
        Libro libro = new Libro();
        Autor autor = autorRepo.findById(idAutor).get();
        Editorial editorial = editorialRepo.findById(idEditorial).get();

        // setteo atributos
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setDescripcion(descripcion);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepo.save(libro);
    }

    // método para listar
    public List<Libro> listarTodo() {
        return libroRepo.findAll();
    }

    // método para editar Libro:
    @Transactional
    public void modificar(Long isbn, String titulo, Integer ejemplares, String descripcion, Long idAutor,
            Long idEditorial)
            throws MyException {

        validar(isbn, titulo, ejemplares, idAutor, idEditorial);

        // este objeto Optional me devuelve un true or false dependiendo de si se
        // encontró lo buscado en el repo
        Optional<Libro> request = libroRepo.findById(isbn);
        Optional<Autor> requestAutor = autorRepo.findById(idAutor);
        Optional<Editorial> requestEditorial = editorialRepo.findById(idEditorial);

        // instancias que necesito por scoope:
        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (requestAutor.isPresent()) {
            autor = requestAutor.get();
        }

        if (requestEditorial.isPresent()) {
            editorial = requestEditorial.get();
        }

        if (request.isPresent()) {
            Libro libro = request.get(); // le asigno a libro lo que se encontró en la request

            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setEjemplares(ejemplares);
            libro.setDescripcion(descripcion);

            // persistir:
            libroRepo.save(libro);
        }
    }

    @Transactional
    public void agregarDescripcion(String descripcion) {

    }

    @Transactional
    public void eliminar(Long isbn) throws MyException {

        Optional<Libro> request = libroRepo.findById(isbn);

        if (request.isPresent()) {
            libroRepo.delete(request.get());
        } else {
            throw new MyException("Error, no se eliminó correctamente.");
        }

    }

    public Libro getOne(Long isbn) {
        return libroRepo.getReferenceById(isbn);
    }

    public List<Libro> buscarLibros(String busqueda) {
        return libroRepo.buscarPorFiltros(busqueda);
    }

    // método para validar datos:
    private void validar(Long isbn, String titulo, Integer ejemplares,
            Long idAutor, Long idEditorial) throws MyException {

        if (isbn == null) {
            throw new MyException("El ISBN no puede ser nulo.");
        }

        if (titulo.isEmpty() || titulo == null) {
            throw new MyException("El titulo no puede ser nulo.");
        }

        if (ejemplares == null) {
            throw new MyException("Ejemlares no puede ser nulo.");
        }

        if (idAutor == null || !autorRepo.existsById(idAutor)) {
            throw new MyException("No se encuentra el autor.");
        }

        if (idEditorial == null || !editorialRepo.existsById(idEditorial)) {
            throw new MyException("No se encuentra la editorial.");
        }
    }

}
