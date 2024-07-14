package com.myapp.Libreria.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.Libreria.entities.Editorial;
import com.myapp.Libreria.exceptions.MyException;
import com.myapp.Libreria.repositories.EditorialRepo;

import jakarta.transaction.Transactional;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepo editorialRepo;

    @Transactional
    public void crear(String nombre) throws MyException {

        Editorial editorial = new Editorial();

        editorial.setNombre(nombre);

        editorialRepo.save(editorial);

    }

    public List<Editorial> listar() {
        return editorialRepo.findAll();
    }

    @Transactional
    public void modificar(Integer id, String nombre) throws MyException {

        validar(id, nombre);

        Optional<Editorial> request = editorialRepo.findById(id);

        if (request.isPresent()) {
            Editorial editorial = request.get();
            editorial.setNombre(nombre);
            editorialRepo.save(editorial);
        }

    }

    public void validar(Integer id, String nombre) throws MyException {
        if (id == null) {
            throw new MyException("El ID no puede ser nulo.");
        }

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("El nombre no puede ser nulo.");
        }
    }

}
