package com.example.noticia.repositorios;

import com.example.noticia.entidades.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Optional<Usuario> buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    public Optional<Usuario> buscarPorNombreValidar(@Param("nombreUsuario")String nombreUsuario);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    public Usuario buscarPorNombre(@Param("nombreUsuario")String nombreUsuario);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'USUARIO'")
    public List<Usuario> buscarUsuarios();
    
}
