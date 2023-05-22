
package com.egg.estancias.repositorios;



import com.egg.estancias.entidades.Casa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasaRepositorio extends JpaRepository<Casa, String>{
    @Query("SELECT c FROM Casa c WHERE c.familia.id = :idFamilia")
    public List<Casa> buscarCasasPorFamiliaId(@Param("idFamilia") String idFamilia);
}
