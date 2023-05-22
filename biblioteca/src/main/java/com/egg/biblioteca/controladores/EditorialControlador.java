package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "editorial_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {

        try {
            editorialServicio.crearEditorial(nombre);
            modelo.put("exito", "La Editorial fue cargado correctamente!");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "editorial_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("editoriales", editoriales);
        return "editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("editorial", editorialServicio.getOne(id));
        return "editorial_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, RedirectAttributes redirectAttributes) {
        try {
            editorialServicio.modificarEditorial(id, nombre);
            redirectAttributes.addFlashAttribute("exito", "El autor fue cargado correctamente!");
            return "redirect:../lista";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/editorial/modificar/{id}";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            List<String> idEditorialesRelacionados = editorialServicio.validarRelaciones();
            if (idEditorialesRelacionados.contains(id)) {
                throw new MiException("No puedes eliminar una editorial relacionada a un libro. Elimine primero el libro.");
            }
            editorialServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "La editorial fue eliminada!");
            return "redirect:/editorial/lista";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/editorial/lista";
        }
    }
}
