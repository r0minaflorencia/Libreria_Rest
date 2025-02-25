package com.libreria.services;

import com.libreria.entities.Editorial;
import com.libreria.exceptions.MyException;
import com.libreria.repositories.EditorialRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepo editorialRepo;

    @Transactional
    public void crear(String nombre) throws MyException {

        validar(nombre);

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorialRepo.save(editorial);
    }

    public List<Editorial> listarTodo() {
        return editorialRepo.findAll();
    }

    @Transactional
    public void modificar(Long id, String nombre) throws MyException {

        validar(nombre);

        Optional<Editorial> request = editorialRepo.findById(id);

        if (request.isPresent()) {

            Editorial editorial = request.get();
            editorial.setNombre(nombre);
            editorialRepo.save(editorial);
        }
    }

    @Transactional
    public void eliminar(Long id) throws MyException {
        Optional<Editorial> request = editorialRepo.findById(id);

        if (request.isPresent()) {
            Editorial editorial = request.get();
            editorialRepo.delete(editorial);
        }

    }

    public Editorial getOne(Long id) {
        return editorialRepo.getReferenceById(id);
    }

    private void validar(String nombre) throws MyException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("El nombre no puede ser nulo.");
        }
    }
}
