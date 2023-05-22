package com.egg.estancias.repositorios;

import com.egg.estancias.entidades.Estancia;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EstanciaRepositorio extends JpaRepository<Estancia, String> {
    @Query("SELECT e FROM Estancia e WHERE e.cliente.id = :idCliente")
    public List<Estancia> buscarEstanciaPorClienteId(@Param("idCliente") String idCliente);
    
    @Query("SELECT e FROM Estancia e WHERE e.casa.id = :idCasa AND"
            + " ((e.fechaDesde BETWEEN :fechaDesde AND :fechaHasta) OR (e.fechaHasta BETWEEN :fechaDesde AND :fechaHasta))")
    public List<Estancia> buscarReservas(@Param("idCasa") String idCasa,
            @Param("fechaDesde") Date fechaDesde, @Param("fechaHasta") Date fechaHasta);
    
   
}
