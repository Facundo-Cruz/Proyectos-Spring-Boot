package com.egg.estancias.servicios;

import com.egg.estancias.entidades.Usuario;
import com.egg.estancias.enumeraciones.Rol;
import com.egg.estancias.excepciones.MiException;
import com.egg.estancias.repositorios.ClienteRepositorio;
import com.egg.estancias.repositorios.FamiliaRepositorio;
import com.egg.estancias.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FamiliaRepositorio familiaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void crearUsuario(String alias, String email, String clave, String clave2) throws MiException {

        validar(alias, email, clave, clave2);

        Usuario usuario = new Usuario();

        usuario.setAlias(alias);
        usuario.setEmail(email);
        usuario.setClave(new BCryptPasswordEncoder().encode(clave));
        usuario.setFechaAlta(new Date());
        usuario.setEstado(true);
        usuario.setRol(Rol.ADMIN);

        usuarioRepositorio.save(usuario);

    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findByRol(Rol.ADMIN);
    }

    @Transactional
    public void eliminarUsuario(String id) {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuarioRepositorio.delete(usuario);

        }
    }

    public void darBajaUsuario(String id) {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setFechaBaja(new Date());
            usuario.setEstado(false);

            usuarioRepositorio.save(usuario);
        }

    }

    public void darAltaUsuario(String id) {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setFechaBaja(null);
            usuario.setEstado(true);
            usuario.setFechaAlta(new Date());

            usuarioRepositorio.save(usuario);
        }

    }

    @Transactional
    public void cambiarClave(String claveActual, String id, String clave, String clave2) throws MiException {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            if (clave.isEmpty() || clave == null || clave.length() <= 5) {
                throw new MiException("La contraseña no puede ser vacía y debe contener más de 5 caracteres.");
            }

            if (!clave.equals(clave)) {
                throw new MiException("Las contraseñás no coinciden.");
            }

            Usuario usuario = respuesta.get();

            String encodedPassword = usuario.getClave();

            if (bCryptPasswordEncoder.matches(claveActual, encodedPassword)) {
                usuario.setClave(new BCryptPasswordEncoder().encode(clave));

                usuarioRepositorio.save(usuario);
            } else {
                throw new MiException("Las contraseña actual no es correcta.");
            }

        }

    }

    protected void validar(String alias, String email, String clave, String clave2) throws MiException {

        if (email == null || email.trim().isEmpty()) {

            throw new MiException("Tu correo no puede ser nulo o vacío.");

        }

        Optional<Usuario> respuesta = usuarioRepositorio.buscarPorEmail(email);
        if (respuesta.isPresent()) {
            throw new MiException("Lo sentimos, ese email ya ha sido registrado.");
        }

        if (alias == null || alias.trim().isEmpty()) {
            throw new MiException("Tu alias no puede ser nulo o vacío.");
        }

        respuesta = usuarioRepositorio.buscarPorAlias(alias);
        if (respuesta.isPresent()) {
            throw new MiException("Lo sentimos, ese alias ya ha sido registrado.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 5) {
            throw new MiException("La contraseña no puede ser vacía y debe contener más de 5 caracteres.");
        }

        if (!clave.equals(clave2)) {
            throw new MiException("Las contraseñás no coinciden.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmailUser(email);

        if (usuario == null) {
            usuario = familiaRepositorio.buscarPorEmail(email);
        }

        if (usuario == null) {
            usuario = clienteRepositorio.buscarPorEmail(email);
        }

        if (usuario != null && usuario.isEstado()) {

            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }

    }

}
