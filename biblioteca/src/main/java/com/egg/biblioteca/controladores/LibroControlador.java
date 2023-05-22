package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private EditorialServicio editorialServicio;
    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {

        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El Libro fue cargado correctamente!");
        } catch (MiException ex) {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());
            return "libro_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);
        return "libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        Libro libro = libroServicio.getOne(isbn);
        modelo.put("libro", libro);
        
        List<Autor> autores = autorServicio.listarAutores();
        autores.remove(libro.getAutor());
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        editoriales.remove(libro.getEditorial());
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        
        return "libro_modificar.html";
        
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("isbn: " + isbn +", titulo: " + titulo + ", ejemplares: " + ejemplares
             + ", idAutor: " + idAutor + ", idEditorial: " + idEditorial);
            libroServicio.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            redirectAttributes.addFlashAttribute("exito", "El autor fue cargado correctamente!");
            return "redirect:../lista";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/libro/modificar/{isbn}";
        }
    }
    
    @GetMapping("/eliminar/{isbn}")
    public String eliminar(@PathVariable Long isbn, RedirectAttributes redirectAttributes) {
        
        try {
            libroServicio.eliminar(isbn);
            redirectAttributes.addFlashAttribute("exito", "El libro fue eliminado!");
            return "redirect:/libro/lista";
        } catch (MiException ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "redirect:/libro/lista";
        }
    }

}
