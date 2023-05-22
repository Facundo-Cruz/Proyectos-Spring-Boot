package com.egg.estancias.servicios;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Familia;
import com.egg.estancias.entidades.Imagen;
import com.egg.estancias.excepciones.MiException;
import com.egg.estancias.repositorios.CasaRepositorio;
import com.egg.estancias.repositorios.FamiliaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CasaServicio {

    @Autowired
    private CasaRepositorio casaRepositorio;

    @Autowired
    private FamiliaRepositorio familiaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void crearCasa(MultipartFile archivo, String idFamilia, String calle, int numero, String codPostal,
            String ciudad, String pais, Date fechaDesde, Date fechaHasta, int minDias,
            int maxDias, double precio, String tipoVivienda) throws MiException {

        validar(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);

        Optional<Familia> respuesta = familiaRepositorio.findById(idFamilia);

        if (respuesta.isPresent()) {

            Familia familia = respuesta.get();

            Casa casa = new Casa();
            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            casa.setFamilia(familia);
            Imagen imagen = imagenServicio.guardar(archivo);
            casa.setImagen(imagen);
            casaRepositorio.save(casa);
            
            List<Casa> casas = familia.getCasas();
            familia.setCasas(casas);
            familiaRepositorio.save(familia);

        }

    }

    public List<Casa> listarCasas() {
        return casaRepositorio.findAll();
    }
    
    @Transactional
    public void modificarCasa(MultipartFile archivo, String id, String calle, int numero, String codPostal,
            String ciudad, String pais, Date fechaDesde, Date fechaHasta, int minDias,
            int maxDias, double precio, String tipoVivienda) throws MiException {

        Optional<Casa> respuesta = casaRepositorio.findById(id);

        if (respuesta.isPresent()) {

            validar(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);

            Casa casa = respuesta.get();
            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setCiudad(ciudad);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
            
            String idImagen = null;
            if (casa.getImagen() != null) {
                idImagen = casa.getImagen().getId();
            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            casa.setImagen(imagen);
            casaRepositorio.save(casa);

        }
    }
    
    @Transactional
    public void eliminarCasa(String id) {

        Optional<Casa> respuesta = casaRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Casa casa = respuesta.get();

            Familia familia = casa.getFamilia();

            familia.getCasas().remove(casa);

            familiaRepositorio.save(familia);
            casaRepositorio.delete(casa);
            
        }
    }

    public List<Casa> trarCasasDeFamilia(String id){
        return casaRepositorio.buscarCasasPorFamiliaId(id);
    }
    
    public Casa casaPorId(String id){
        return casaRepositorio.getById(id);
    }
    
    private void validar(String calle, int numero, String codPostal,
            String ciudad, String pais, Date fechaDesde, Date fechaHasta, int minDias,
            int maxDias, double precio, String tipoVivienda) throws MiException {

        if (calle == null || calle.isEmpty()) {
            throw new MiException("La calle no puede ser nula o vacía.");
        }

        if (numero <= 0) {
            throw new MiException("El número de la calle debe ser mayor que cero.");
        }

        if (codPostal == null || codPostal.isEmpty()) {
            throw new MiException("El código postal no puede ser nulo o vacío.");
        }

        if (ciudad == null || ciudad.isEmpty()) {
            throw new MiException("La ciudad no puede ser nula o vacía.");
        }

        if (pais == null || pais.isEmpty()) {
            throw new MiException("El país no puede ser nulo o vacío.");
        }

        if (fechaDesde == null) {
            throw new MiException("La fecha desde no puede ser nula.");
        }

        if (fechaHasta == null) {
            throw new MiException("La fecha hasta no puede ser nula.");
        }

        if (fechaDesde.after(fechaHasta)) {
            throw new MiException("La fecha desde no puede ser posterior a la fecha hasta.");
        }

        if (minDias < 1) {
            throw new MiException("La cantidad mínima de días debe ser al menos 1.");
        }

        if (maxDias < minDias) {
            throw new MiException("La cantidad máxima de días debe ser mayor o igual a la cantidad mínima de días.");
        }

        if (precio <= 0) {
            throw new MiException("El precio debe ser mayor que cero.");
        }

        if (tipoVivienda == null || tipoVivienda.isEmpty()) {
            throw new MiException("El tipo de vivienda no puede ser nulo o vacío.");
        }
    }
}
