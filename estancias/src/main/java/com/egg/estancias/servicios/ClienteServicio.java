package com.egg.estancias.servicios;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Cliente;
import com.egg.estancias.entidades.Estancia;
import com.egg.estancias.enumeraciones.Rol;
import com.egg.estancias.excepciones.MiException;
import com.egg.estancias.repositorios.CasaRepositorio;
import com.egg.estancias.repositorios.ClienteRepositorio;
import com.egg.estancias.repositorios.EstanciaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServicio extends UsuarioServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Autowired
    private EstanciaRepositorio estanciaRepositorio;
    
    @Autowired
    private CasaRepositorio casaRepositorio;
    
    @Transactional
    public void crearCliente(String alias, String email, String clave, String clave2,
            String calle, Integer numero, String codPostal, String ciudad, String pais) throws MiException {
        
        validar(alias, email, clave, clave2, calle, numero, codPostal, ciudad, pais);
        
        Cliente cliente = new Cliente();

        cliente.setAlias(alias);
        cliente.setEmail(email);
        cliente.setClave(new BCryptPasswordEncoder().encode(clave));
        cliente.setFechaAlta(new Date());
        cliente.setEstado(true);
        cliente.setRol(Rol.CLIENTE);
        cliente.setCalle(calle);
        cliente.setNumero(numero);
        cliente.setCodPostal(codPostal);
        cliente.setCiudad(ciudad);
        cliente.setPais(pais);
        cliente.setEstancias(new ArrayList());
        
        clienteRepositorio.save(cliente);

    }
    
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }
    
    @Transactional
    public void modificarCliente(String id, String alias, String email, String clave, String clave2,
            String calle, Integer numero, String codPostal, String ciudad, String pais) throws MiException {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            validar(alias, email, clave, clave2, calle, numero, codPostal, ciudad, pais);

            Cliente cliente = respuesta.get();

            cliente.setAlias(alias);
            cliente.setEmail(email);
            cliente.setClave(new BCryptPasswordEncoder().encode(clave));
            cliente.setCalle(calle);
            cliente.setNumero(numero);
            cliente.setCodPostal(codPostal);
            cliente.setCiudad(ciudad);
            cliente.setPais(pais);

            clienteRepositorio.save(cliente);
            
        }
    }
    
    @Transactional
    public void eliminarCliente(String id) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            
            clienteRepositorio.delete(cliente);

        }
    }

    public void darBajaCliente(String id) {
        
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Cliente cliente = respuesta.get();
            
            cliente.setFechaBaja(new Date());
            cliente.setEstado(false);
            
            clienteRepositorio.save(cliente);
        }
        
    }
    
    public void darAltaCliente(String id) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

             Cliente cliente = respuesta.get();

            cliente.setFechaBaja(null);
            cliente.setEstado(true);
            cliente.setFechaAlta(new Date());

            clienteRepositorio.save(cliente);
        }

    }
    
    private void validar(String alias, String email, String clave, String clave2,
            String calle, Integer numero, String codPostal, String ciudad, String pais) throws MiException {
        
        super.validar(alias, email, clave, clave2);

        if (calle == null || calle.trim().isEmpty()) {
            throw new MiException("La calle no puede ser nula ni vacío.");
        }

        if (numero == null || numero <= 0) {
            throw new MiException("El número debe ser positivo.");
        }

        if (codPostal == null || codPostal.isEmpty()) {
            throw new MiException("El código postal no puede ser nulo ni vacío.");
        }

        if (ciudad == null || ciudad.isEmpty()) {
            throw new MiException("La ciudad no puede ser nula ni vacío.");
        }

        if (pais == null || pais.isEmpty()) {
            throw new MiException("El pais no puede ser nulo ni vacío.");
        }
    }
}
