package com.libreria.repositories;

import com.libreria.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepo extends JpaRepository<Autor, Long> {
    
    // Definición de consulta utilizando JPQL
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    Autor buscarPorNombre(@Param("nombre") String nombre);
}
