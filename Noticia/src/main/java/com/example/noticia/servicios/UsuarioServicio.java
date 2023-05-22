package com.example.noticia.servicios;

import com.example.noticia.entidades.Administrador;
import com.example.noticia.entidades.Periodista;
import com.example.noticia.entidades.Usuario;
import com.example.noticia.enumeraciones.Rol;
import com.example.noticia.excepciones.MiException;
import com.example.noticia.repositorios.AdministradorRepositorio;
import com.example.noticia.repositorios.PeriodistaRepositorio;
import com.example.noticia.repositorios.UsuarioRepositorio;
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
    private PeriodistaRepositorio periodistaRepositorio;

    @Autowired
    private AdministradorRepositorio administradorRepositorio;

    @Transactional
    public void registrar(String email, String nombreUsuario, String password, String password2) throws MiException {
        validar(email, nombreUsuario, password, password2);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setFechaAlta(new Date());
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USUARIO);
        usuario.setActivo(true);

        usuarioRepositorio.save(usuario);
    }

    private void validar(String email, String nombreUsuario, String password, String password2) throws MiException {
        if (email == null || email.isEmpty()) {
            throw new MiException("El email no puede ser nulo o vacío.");
        }
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new MiException("El nombre de usuario no puede ser nulo o vacío.");
        }
        if (password.isEmpty() || password == null) {
            throw new MiException("La contraseña no puede estar nula o vacía.");
        }
        if (password.length() <= 5) {
            throw new MiException("La contraseña debe tener contener más de 5 dígitos.");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas no coinciden.");
        }

        Optional<Usuario> usuario = usuarioRepositorio.buscarPorEmail(email);
        Optional<Periodista> periodista = periodistaRepositorio.buscarPorEmail(email);
        Optional<Administrador> administrador = administradorRepositorio.buscarPorEmail(email);
        
        if (usuario.isPresent() || periodista.isPresent() || administrador.isPresent()) {
            throw new MiException("Ese correo electrónico ya corresponde a un usuario.");
        }

        usuario = usuarioRepositorio.buscarPorNombreValidar(nombreUsuario);
        periodista = periodistaRepositorio.buscarPorNombreValidar(nombreUsuario);
        administrador = administradorRepositorio.buscarPorNombreValidar(nombreUsuario);

        if (usuario.isPresent() || periodista.isPresent() || administrador.isPresent()) {
            throw new MiException("Ese nombre ya corresponde a un usuario.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorNombre(nombreUsuario);

        if (usuario == null) {
            usuario = periodistaRepositorio.buscarPorNombre(nombreUsuario);
        }

        if (usuario == null) {
            usuario = administradorRepositorio.buscarPorNombre(nombreUsuario);
        }

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            
            User user = new User(usuario.getNombreUsuario(), usuario.getPassword(), permisos);
            return user;
        } else {
            return null;
        }

    }
}
