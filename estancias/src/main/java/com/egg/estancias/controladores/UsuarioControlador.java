package com.egg.estancias.controladores;

import com.egg.estancias.entidades.Usuario;
import com.egg.estancias.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/admin")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/lista")
    public String listarUsuarios(ModelMap model) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        model.put("usuarios", usuarios);
        return "usuario_list";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            usuarioServicio.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("exito", "El usuario ha sido eliminado correctamente.");
            return "redirect:/admin/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/lista";
        }

    }

    @GetMapping("/darBaja/{id}/{estado}")
    public String darBaja(@PathVariable String id, @PathVariable Boolean estado, RedirectAttributes redirectAttributes) {
        try {

            if (estado) {
                usuarioServicio.darBajaUsuario(id);
                redirectAttributes.addFlashAttribute("exito", "El usuario ha sido dado de baja correctamente.");
            } else {
                usuarioServicio.darAltaUsuario(id);
                redirectAttributes.addFlashAttribute("exito", "El usuario ha sido dado de alta correctamente.");
            }

            return "redirect:/admin/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/lista";
        }
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "usuario_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String alias,
            @RequestParam String clave, @RequestParam String clave2, ModelMap model) {
        try {
            usuarioServicio.crearUsuario(alias, email, clave, clave2);
            model.put("exito", "El usuario ha sido registrado correctamente.");
            return "usuario_form.html";
        } catch (Exception e) {
            model.put("email", email);
            model.put("alias", alias);
            model.put("error", e.getMessage());
            return "usuario_form.html";
        }
    }

    @GetMapping("/perfil")
    public String perfil(ModelMap model) {
        return "usuario_modificar.html";
    }

    @PostMapping("/perfil/clave")
    public String perfil(@RequestParam String claveActual, @RequestParam String id, @RequestParam String clave,
            @RequestParam String clave2, ModelMap model) {
        try {
            usuarioServicio.cambiarClave(claveActual, id, clave, clave2);
            model.put("exito", "La contraseña ha sido actualizada correctamente.");
            return "usuario_modificar.html";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "usuario_modificar.html";
        }
    }
}
