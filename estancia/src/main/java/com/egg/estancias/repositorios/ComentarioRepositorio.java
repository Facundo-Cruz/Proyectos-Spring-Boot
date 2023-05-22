package com.egg.estancias.repositorios;

//import com.egg.estancias.entidades.Comentario;
import com.egg.estancias.entidades.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {
 
    @Query("SELECT c FROM Comentario c WHERE c.casa.id = :idCasa")
    public List<Comentario> buscarPorId(@Param("idCasa") String idCasa);
}
