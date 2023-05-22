package com.example.noticia.repositorios;

import com.example.noticia.entidades.Administrador;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepositorio extends JpaRepository<Administrador, String>{
    
    @Query("SELECT a FROM Administrador a WHERE a.email = :email")
    public Optional<Administrador> buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT a FROM Administrador a WHERE a.nombreUsuario = :nombreUsuario")
    public Optional<Administrador> buscarPorNombreValidar(@Param("nombreUsuario")String nombreUsuario);
    
    @Query("SELECT a FROM Administrador a WHERE a.nombreUsuario = :nombreUsuario")
    public Administrador buscarPorNombre(@Param("nombreUsuario")String nombreUsuario);
}
