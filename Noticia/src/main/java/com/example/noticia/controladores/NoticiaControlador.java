package com.example.noticia.controladores;

import com.example.noticia.entidades.Noticia;
import com.example.noticia.excepciones.MiException;
import com.example.noticia.repositorios.NoticiaRepositorio;
import com.example.noticia.servicios.NoticiaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/registrar") // localhost8080/autor/registrar
    public String registrar() {
        return "noticia_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String titulo, @RequestParam String cuerpo, @RequestParam String id, ModelMap modelo) {
 
        try {
            noticiaServicio.crearNoticia(titulo, cuerpo, id);
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        }
        return "redirect:/inicio";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        return "noticia_list.html";
    }

    @GetMapping("/mostrar/{id}/{page}")
    public String mostrar(@PathVariable String id,@PathVariable String page, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(id));
        modelo.put("page", page);
        return "noticia_mostrar.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String titulo, String cuerpo, RedirectAttributes redirectAttributes) {
        try {

            noticiaServicio.modificarNoticia(id, titulo, cuerpo);
            redirectAttributes.addFlashAttribute("exito", "!La noticia fue actualizada correctamente!");
            return "redirect:../lista";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            System.out.println(ex.getMessage());
            return "redirect:/noticia/modificar/{id}";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            String titulo = noticiaServicio.getTitulo(id);
            noticiaServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "!La noticia '" + titulo + "' fue eliminada!");
            return "redirect:/noticia/lista";
        } catch (MiException ex) {
            return "redirect:/noticia/lista";
        }
    }
}
