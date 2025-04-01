package com.libreria.repositories;

import com.libreria.entities.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepo extends JpaRepository<Libro, Long> {

        // Buscar libros por título
        @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
        List<Libro> buscarPorTitulo(@Param("titulo") String titulo);

        // Buscar libros por nombre del autor
        @Query("SELECT l FROM Libro l JOIN l.autor a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :autor, '%'))")
        List<Libro> buscarPorAutor(@Param("autor") String autor);

        // Buscar libros por nombre de la editorial
        @Query("SELECT l FROM Libro l JOIN l.editorial e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :editorial, '%'))")
        List<Libro> buscarPorEditorial(@Param("editorial") String editorial);

        @Query("SELECT l FROM Libro l WHERE " +
                        "(LOWER(l.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                        " LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                        " LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
        List<Libro> buscarPorFiltros(@Param("busqueda") String busqueda);

}
