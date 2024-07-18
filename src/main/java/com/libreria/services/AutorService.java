package com.libreria.services;

import com.libreria.entities.Autor;
import com.libreria.exceptions.MyException;
import com.libreria.repositories.AutorRepo;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    @Autowired
    private AutorRepo autorRepo;

    @Transactional
    public void crear(String nombre) throws MyException {
        
        validar(nombre);
        
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepo.save(autor);
    }

    public List<Autor> listarTodo() {
        return autorRepo.findAll();
    }

    @Transactional
    public void modificar(Long id, String nombre) throws MyException {

        validar(nombre);

        Optional<Autor> request = autorRepo.findById(id);

        if (request.isPresent()) {
            Autor autor = request.get();
            autor.setNombre(nombre);
            autorRepo.save(autor);
        }
    }

    private void validar(String nombre) throws MyException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("El nombre no puede estar vacío.");
        }
    }

}
