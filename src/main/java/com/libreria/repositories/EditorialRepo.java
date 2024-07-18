package com.libreria.repositories;

import com.libreria.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepo extends JpaRepository<Editorial, Long> {

    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarPorNombre(@Param("nombre") String nombre);
}
