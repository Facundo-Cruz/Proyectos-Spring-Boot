package com.egg.estancias.repositorios;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Familia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaRepositorio extends JpaRepository<Familia, String> {
    
    @Query("SELECT f FROM Familia f WHERE f.email = :email")
    public Familia buscarPorEmail(@Param("email")String email);
    

}
