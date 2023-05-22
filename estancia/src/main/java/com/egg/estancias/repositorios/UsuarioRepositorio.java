package com.egg.estancias.repositorios;

import com.egg.estancias.entidades.Usuario;
import com.egg.estancias.enumeraciones.Rol;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
    @Query("SELECT u FROM Usuario u WHERE u.alias = :alias")
    public Optional<Usuario> buscarPorAlias(@Param("alias")String alias);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmailUser(@Param("email")String email);
    
    List<Usuario> findByRol(Rol rol);
}
