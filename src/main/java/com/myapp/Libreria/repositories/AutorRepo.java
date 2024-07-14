package com.myapp.Libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myapp.Libreria.entities.Autor;

@Repository
public interface AutorRepo extends JpaRepository<Autor, Integer> {

    @Query("SELECT a FROM Autor WHERE a.nombre = :nombre")
    public Autor buscarPorNombre(@Param("nombre") String nombre);
    
}
