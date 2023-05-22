package com.example.noticia.repositorios;

import com.example.noticia.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//El repositorio que persiste a esta entidad (NoticiaRepositorio) debe contener los métodos
//necesarios para guardar/actualizar noticias en la base de datos, realizar consultas o dar de baja
//según corresponda.

@Repository
public interface NoticiaRepositorio  extends JpaRepository<Noticia, String>{
    @Query("SELECT n FROM Noticia n ORDER BY n.fecha DESC")
    public List<Noticia> listarPorAntiguedad();
    
 
//    @Query(value = "select * from Noticia order by id desc", nativeQuery = true)
//    List<Noticia> findAllOrderByidDesc();
//    
//    @Query(value = "select * from Noticia order by id asc", nativeQuery = true)
//    List<Noticia> findAllOrderByidAsc();
    
  
    
    
}
