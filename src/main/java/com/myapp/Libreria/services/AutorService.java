package com.myapp.Libreria.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.Libreria.entities.Autor;
import com.myapp.Libreria.exceptions.MyException;
import com.myapp.Libreria.repositories.AutorRepo;

import jakarta.transaction.Transactional;

@Service
public class AutorService {

    @Autowired
    private AutorRepo autorRepo;

    @Transactional
    public void crear(String nombre) {

        Autor autor = new Autor();

        autor.setNombre(nombre);

        autorRepo.save(autor);
    }

    public List<Autor> listar() {
        return autorRepo.findAll();
    }

    @Transactional
    public void modificar(Integer id, String nombre) throws MyException {

        validar(id, nombre);

        Optional<Autor> request = autorRepo.findById(id);

        if (request.isPresent()) {
            Autor autor = request.get();
            autor.setNombre(nombre);
            autorRepo.save(autor);
        }
    }

    private void validar(Integer id, String nombre) throws MyException {

        if (id == null) {
            throw new MyException("El ID no puede ser nulo.");
        }

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("El nombre no puede ser nulo.");
        }
    }

}
