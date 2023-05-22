package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;
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
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/registrar") // localhost8080/autor/registrar
    public String registrar() {
        return "autor_form.html";
    }
    @GetMapping("/prueba") // localhost8080/autor/registrar
    public String prueba(ModelMap modelo) {
        modelo.put("autores", autorServicio.listarAutores());
        return "prueba.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            autorServicio.crearAutor(nombre);
            modelo.put("exito", "El autor fue cargado correctamente!");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "autor_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("autor", autorServicio.getOne(id));
        return "autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, RedirectAttributes redirectAttributes) {
        try {
            autorServicio.modificarAutor(nombre, id);
            redirectAttributes.addFlashAttribute("exito", "El autor fue cargado correctamente!");
            return "redirect:../lista";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/autor/modificar/{id}";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            System.out.println("--------------");
            System.out.println(id);
            System.out.println("--------------");
            List<String> idAutoresRelacionados = autorServicio.validarRelaciones();
            if (idAutoresRelacionados.contains(id)) {
                throw new MiException("No puedes eliminar al autor " + autorServicio.getOne(id).getNombre() + " porque está relacionado a un libro. Elimine primero el libro.");
            }

            autorServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "El autor fue eliminado!");
            return "redirect:/autor/lista";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/autor/lista";
        }
    }

   
}
