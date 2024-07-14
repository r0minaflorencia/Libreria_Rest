package com.myapp.Libreria.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.Libreria.entities.Autor;
import com.myapp.Libreria.entities.Editorial;
import com.myapp.Libreria.entities.Libro;
import com.myapp.Libreria.exceptions.MyException;
import com.myapp.Libreria.repositories.AutorRepo;
import com.myapp.Libreria.repositories.EditorialRepo;
import com.myapp.Libreria.repositories.LibroRepo;

import jakarta.transaction.Transactional;

@Service
public class LibroService {

    // inyeccion de dependencias:
    @Autowired
    private LibroRepo libroRepo;
    @Autowired
    private AutorRepo autorRepo;
    @Autowired
    private EditorialRepo editorialRepo;

    @Transactional
    public void crear(Long isbn, String titulo, Integer autorId, Integer editorialId) throws MyException {

        validar(isbn, titulo, editorialId, autorId, editorialId);

        Libro libro = new Libro();
        Autor autor = autorRepo.findById(autorId).get();
        Editorial editorial = editorialRepo.findById(editorialId).get();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAlta(new Date());
        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepo.save(libro);

    }

    public List<Libro> listar() {
        return libroRepo.findAll();
    }

    @Transactional
    public void modificar(Long isbn, String titulo, Integer ejemplares, Integer autorId, Integer editorialId)
            throws MyException {

        validar(isbn, titulo, ejemplares, autorId, editorialId);

        Optional<Libro> request = libroRepo.findById(isbn);
        Optional<Autor> autorRequest = autorRepo.findById(autorId);
        Optional<Editorial> editorialRequest = editorialRepo.findById(editorialId);

        // instancias por scoope:
        Autor a = new Autor();
        Editorial e = new Editorial();

        if (autorRequest.isPresent()) {
            a = autorRequest.get();
        }

        if (editorialRequest.isPresent()) {
            e = editorialRequest.get();
        }

        if (request.isPresent()) {

            Libro libro = request.get();

            libro.setTitulo(titulo);
            libro.setAutor(a);
            libro.setEditorial(e);
            libro.setEjemplares(ejemplares);

            // persist:
            libroRepo.save(libro);
        }

    }

    public void validar(Long isbn, String titulo, Integer ejemplares, Integer autorId, Integer editorialId)
            throws MyException {

        if (isbn == null) {
            throw new MyException("El ISBN no puede ser nulo.");
        }

        if (titulo.isEmpty() || titulo == null) {
            throw new MyException("El titulo no puede ser nulo.");
        }

        if (ejemplares == null) {
            throw new MyException("Ejemplares no puede ser nulo.");
        }

        if (autorId == null) {
            throw new MyException("No se encuentra el autor.");
        }

        if (editorialId == null) {
            throw new MyException("No se encuentra la editorial.");
        }

    }

}
