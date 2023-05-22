package com.example.noticia.servicios;

import com.example.noticia.entidades.Periodista;
import com.example.noticia.entidades.Usuario;
import com.example.noticia.enumeraciones.Rol;
import com.example.noticia.excepciones.MiException;
import com.example.noticia.repositorios.PeriodistaRepositorio;
import com.example.noticia.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PeriodistaServicio {

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearPeriodista(int sueldoMensual, String id) throws MiException {
        if (sueldoMensual <= 0) {
            throw new MiException("El sueldo no puede ser negativo o 0");
        }
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        Usuario usuario = respuesta.get();
        Periodista periodista = new Periodista();

        periodista.setId(usuario.getId());
        periodista.setNombreUsuario(usuario.getNombreUsuario());
        periodista.setEmail(usuario.getEmail());
        periodista.setFechaAlta(usuario.getFechaAlta());
        periodista.setActivo(usuario.getActivo());
        periodista.setPassword(usuario.getPassword());
        periodista.setRol(Rol.PERIODISTA);
        periodista.setSueldoMensual(sueldoMensual);
        periodista.setMisNoticias(new ArrayList());

        periodistaRepositorio.save(periodista);
        usuarioRepositorio.deleteById(id);
    }
    
    @Transactional
    public void actualizarSueldo(int sueldoMensual, String id) throws MiException {
        if (sueldoMensual <= 0) {
            throw new MiException("El sueldo no puede ser negativo o 0");
        }
        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Periodista periodista = respuesta.get();
            periodista.setSueldoMensual(sueldoMensual);
            periodistaRepositorio.save(periodista);
        }
    }
    
    @Transactional
    public void cambiarEstado(String id) {
        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Periodista periodista = respuesta.get();
            periodista.setActivo(!periodista.getActivo());
            periodistaRepositorio.save(periodista);
        }

    }
}
