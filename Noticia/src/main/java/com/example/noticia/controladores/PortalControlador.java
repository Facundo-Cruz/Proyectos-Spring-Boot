package com.example.noticia.controladores;

import com.example.noticia.entidades.Noticia;
import com.example.noticia.entidades.Usuario;
import com.example.noticia.excepciones.MiException;
import com.example.noticia.repositorios.NoticiaRepositorio;
import com.example.noticia.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PERIODISTA', 'ROLE_ADMINISTRADOR')")
    @GetMapping("/inicio")
    public String inicio(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size, Model modelo) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        Page<Noticia> noticias = noticiaRepositorio.findAll(pageable);
        //Eliminó las etiquetas HTML dentro del atributo cuerpo
        for (Noticia noticia : noticias.getContent()) {
            String cuerpoSinHtml = Jsoup.clean(noticia.getCuerpo(), Whitelist.none());
            noticia.setCuerpo(cuerpoSinHtml);
        }
        modelo.addAttribute("noticias", noticias);
        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar() {

        return "registrar.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String nombreUsuario,
            @RequestParam String password, String password2, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {

            usuarioServicio.registrar(email, nombreUsuario, password, password2);

            model.put("exito", "Usuario registrado correctamente.");
            boolean isUser = true;
            model.put("isUser", isUser);
            redirectAttributes.addFlashAttribute("password", password);
            redirectAttributes.addFlashAttribute("nombreUsuario", nombreUsuario);

            return "redirect:/login?isUser=" + isUser;
//            return "index.html";
        } catch (MiException ex) {
            model.put("error", ex.getMessage());
            model.put("email", email);
            model.put("nombreUsuario", nombreUsuario);
            return "registrar.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model, @RequestParam(name = "isUser", defaultValue = "false") boolean isUser) {
        if (error != null) {
            model.put("error", "¡Usuario o contraseña invalidos!");
        }
        model.put("isUser", isUser);
        return "login.html";
    }

}
