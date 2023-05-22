package com.egg.estancias.servicios;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Comentario;
import com.egg.estancias.excepciones.MiException;
import com.egg.estancias.repositorios.CasaRepositorio;
import com.egg.estancias.repositorios.ComentarioRepositorio;
import com.egg.estancias.repositorios.EstanciaRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioServicio {

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    @Autowired
    private CasaRepositorio casaRepositorio;

    public void crearComentario(String alias, String idCasa, String descripción) throws MiException {
        if (descripción == null || descripción.trim().isEmpty()) {
            throw new MiException("El comentario no puede ser nulo o vacío.");
        }
        
        Optional<Casa> respuesta = casaRepositorio.findById(idCasa);

        if (respuesta.isPresent()) {
            Casa casa = respuesta.get();
            Comentario comentario = new Comentario();
            comentario.setDescripcion(descripción);
            comentario.setAlias(alias);
            comentario.setCasa(casa);
            comentarioRepositorio.save(comentario);
        }

    }
    
    public List<Comentario> buscarComentarioPorCasa(String idCasa){
        return comentarioRepositorio.buscarPorId(idCasa);
    }
    
    public void eliminarComentario(String id){
        Optional<Comentario> respuesta = comentarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Comentario comentario = respuesta.get();
            comentarioRepositorio.delete(comentario);
        }
    }
}
