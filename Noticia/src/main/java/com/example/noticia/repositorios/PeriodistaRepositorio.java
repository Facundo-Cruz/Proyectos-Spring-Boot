package com.example.noticia.repositorios;

import com.example.noticia.entidades.Periodista;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodistaRepositorio extends JpaRepository<Periodista,String> {
    
    @Query("SELECT p FROM Periodista p WHERE p.email = :email")
    public Optional<Periodista> buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT p FROM Periodista p WHERE p.nombreUsuario = :nombreUsuario")
    public Optional<Periodista> buscarPorNombreValidar(@Param("nombreUsuario")String nombreUsuario);
    
    @Query("SELECT p FROM Periodista p WHERE p.nombreUsuario = :nombreUsuario")
    public Periodista buscarPorNombre(@Param("nombreUsuario")String nombreUsuario);
}
