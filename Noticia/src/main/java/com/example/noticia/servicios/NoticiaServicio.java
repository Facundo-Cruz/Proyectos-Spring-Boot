package com.example.noticia.servicios;

import com.example.noticia.entidades.Noticia;
import com.example.noticia.entidades.Periodista;
import com.example.noticia.excepciones.MiException;
import com.example.noticia.repositorios.NoticiaRepositorio;
import com.example.noticia.repositorios.PeriodistaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticiaServicio {

    @Autowired
    NoticiaRepositorio noticiaRepositorio;

    @Autowired
    PeriodistaRepositorio periodistaRepositorio;
    
    
    @Transactional
    public void crearNoticia(String titulo, String cuerpo, String id) throws MiException {

        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Periodista periodista = respuesta.get();
            validar(titulo, cuerpo);
            
            Noticia noticia = new Noticia();
            noticia.setCreador(periodista);
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticiaRepositorio.save(noticia);
            
            List<Noticia> noticias = periodista.getMisNoticias();
            noticias.add(noticia);
            periodista.setMisNoticias(noticias);
            periodistaRepositorio.save(periodista);
        }

    }

    @Transactional
    public void modificarNoticia(String id, String titulo, String cuerpo) throws MiException {

        validar(titulo, cuerpo);

        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();

            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);

            noticiaRepositorio.save(noticia);

        }
    }

    @Transactional(readOnly = true)
    public Noticia getOne(String id) {
        return noticiaRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = new ArrayList();

        noticias = noticiaRepositorio.findAll();

        return noticias;
    }

    @Transactional
    public void eliminarNoticia(String id) throws MiException {

        Noticia noticia = noticiaRepositorio.getById(id);

        noticiaRepositorio.delete(noticia);

    }

    @Transactional
    public void eliminar(String id) throws MiException {

        Noticia noticia = noticiaRepositorio.getById(id);

        noticiaRepositorio.delete(noticia);

    }

    public String getTitulo(String id) {
        return noticiaRepositorio.getById(id).getTitulo();
    }

    public void validar(String titulo, String cuerpo) throws MiException {
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El título no puede ser vacío o nulo");
        }

        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiException("El cuerpo no puede ser vacío o nulo");
        }
    }

}
