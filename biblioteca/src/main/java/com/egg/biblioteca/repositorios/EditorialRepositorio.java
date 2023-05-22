package com.egg.biblioteca.repositorios;

import com.egg.biblioteca.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
    @Query("SELECT e.id FROM Editorial e INNER JOIN Libro l ON l.editorial.id = e.id")
    public List<String> buscarRelaciones();
}

