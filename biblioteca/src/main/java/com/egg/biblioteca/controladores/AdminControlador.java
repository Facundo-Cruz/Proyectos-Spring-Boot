package com.egg.biblioteca.controladores;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/dashboard")
    public String panelAdministrativo(){
        return "panel.html";
    }
    
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);

        return "usuario_list";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(RedirectAttributes redirectAttributes, @PathVariable String id) {
        try {
            usuarioServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "El Usuario fue eliminado!");
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("exito", "No se pudo eliminar");
            return "redirect:../usuarios";
        }
        

        return "redirect:../usuarios";
    }
    
    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        usuarioServicio.cambiarRol(id);
        
       return "redirect:/admin/usuarios";
    }
}
