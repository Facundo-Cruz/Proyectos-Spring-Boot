package com.egg.estancias.servicios;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Cliente;
import com.egg.estancias.entidades.Estancia;
import com.egg.estancias.excepciones.MiException;
import com.egg.estancias.repositorios.CasaRepositorio;
import com.egg.estancias.repositorios.ClienteRepositorio;
import com.egg.estancias.repositorios.EstanciaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstanciaServicio {

    @Autowired
    private EstanciaRepositorio estanciaRepositorio;

    @Autowired
    private CasaRepositorio casaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Transactional
    public void crearEstancia(String idCasa, String idCliente, String huesped, Date fechaDesde, Date fechaHasta) throws MiException {

        Optional<Casa> respuestaCasa = casaRepositorio.findById(idCasa);
        Optional<Cliente> respuestaCliente = clienteRepositorio.findById(idCliente);

        if (respuestaCasa.isPresent() && respuestaCliente.isPresent()) {

            validar(idCasa, huesped, fechaDesde, fechaHasta);

            Casa casa = respuestaCasa.get();
            Cliente cliente = respuestaCliente.get();

            Estancia estancia = new Estancia();
            estancia.setCasa(casa);
            estancia.setCliente(cliente);
            estancia.setHuesped(huesped);
            estancia.setFechaDesde(fechaDesde);
            estancia.setFechaHasta(fechaHasta);

            estanciaRepositorio.save(estancia);

        }

    }

    @Transactional
    public void modificarEstancia(String id, String huesped, Date fechaDesde, Date fechaHasta) throws MiException {

        Optional<Estancia> respuesta = estanciaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Estancia estancia = respuesta.get();

            validar(estancia.getCasa().getId(), huesped, fechaDesde, fechaHasta);
            estancia.setHuesped(huesped);
            estancia.setFechaDesde(fechaDesde);
            estancia.setFechaHasta(fechaHasta);

            estanciaRepositorio.save(estancia);

        }
    }

    @Transactional
    public void eliminarEstancia(String id) throws MiException {

        Optional<Estancia> respuesta = estanciaRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Estancia estancia = respuesta.get();
            Date fechaDesde = estancia.getFechaDesde();
            Date fechaActual = new Date();

            if (fechaActual.after(fechaDesde)) {
                throw new MiException("Lo sentimos, no puedes eliminar una estancia que ya se encuentra en marcha.");
            }
            estanciaRepositorio.delete(estancia);

        }

    }

    public Estancia buscarPorId(String id) {
        return estanciaRepositorio.getById(id);
    }

    public List<Estancia> trearEstanciasPorCliente(String id) {
        return estanciaRepositorio.buscarEstanciaPorClienteId(id);
    }

    private void validar(String idCasa, String huesped, Date fechaDesde, Date fechaHasta) throws MiException {

        if (huesped == null || huesped.isEmpty()) {
            throw new MiException("El huesped no puede nulo ni vacío.");
        }

        if (fechaDesde == null) {
            throw new MiException("La fecha desde no puede ser nula");
        }

        if (fechaHasta == null) {
            throw new MiException("La fecha hasta no puede ser nula");
        }

        if (fechaDesde.after(fechaHasta)) {
            throw new MiException("La fecha desde no puede ser posterior a la fecha hasta");
        }

        List<Estancia> estancias = estanciaRepositorio.buscarReservas(idCasa, fechaDesde, fechaHasta);

        if (!estancias.isEmpty()) {
            throw new MiException("Esa fecha seleccionada no se encuentra disponible. Pruebe con otra.");
        }

    }

}
