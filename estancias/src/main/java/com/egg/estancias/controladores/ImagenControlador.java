package com.egg.estancias.controladores;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.servicios.CasaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    private CasaServicio casaServicio;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {
        Casa casa = casaServicio.casaPorId(id);

        byte[] imagen = casa.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}

