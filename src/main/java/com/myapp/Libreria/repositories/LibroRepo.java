package com.myapp.Libreria.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myapp.Libreria.entities.Libro;

@Repository
public interface LibroRepo extends JpaRepository<Libro, Long>{
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombre")
    public List<Libro> buscarPorAutor(@Param("nombre") String nombre);

}
