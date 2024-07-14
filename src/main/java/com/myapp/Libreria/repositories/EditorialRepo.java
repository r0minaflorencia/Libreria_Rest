package com.myapp.Libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myapp.Libreria.entities.Editorial;

public interface EditorialRepo extends JpaRepository<Editorial, Integer>{
    
    @Query("SELECT e FROM Editorial WHERE e.nombre = :nombre")
    public Editorial buscarPorNombre(@Param("nombre") String nombre);

}
