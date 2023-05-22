package com.egg.estancias.repositorios;

import com.egg.estancias.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String>  {
    
    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    public Cliente buscarPorEmail(@Param("email")String email);
    
}
