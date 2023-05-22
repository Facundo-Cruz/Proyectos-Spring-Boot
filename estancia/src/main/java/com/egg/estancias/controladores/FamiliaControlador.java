package com.egg.estancias.controladores;

import com.egg.estancias.entidades.Familia;
import com.egg.estancias.servicios.FamiliaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/familia")
public class FamiliaControlador {

    @Autowired 
    private FamiliaServicio familiaServicio;
    
    @GetMapping("/lista")
    public String listar(ModelMap model){
        List<Familia> familias = familiaServicio.listarFamilias();
        model.put("familias", familias);
        return "familia_list.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes){
        try {
            familiaServicio.eliminarFamilia(id);
            redirectAttributes.addFlashAttribute("exito", "La familia ha sido eliminada correctamente.");
            return "redirect:/familia/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/familia/lista";
        }
    }
    
    @GetMapping("/darBaja/{id}/{estado}")
    public String darBaja(@PathVariable String id, @PathVariable Boolean estado, RedirectAttributes redirectAttributes) {
        try {

            if (estado) {
                familiaServicio.darBajaUsuario(id);
                redirectAttributes.addFlashAttribute("exito", "La familia ha sido dado de baja correctamente.");
            } else {
                familiaServicio.darAltaUsuario(id);
                redirectAttributes.addFlashAttribute("exito", "La familia ha sido dado de alta correctamente.");
            }

            return "redirect:/familia/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/familia/lista";
        }
    }
}
