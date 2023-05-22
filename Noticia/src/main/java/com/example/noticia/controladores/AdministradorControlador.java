package com.example.noticia.controladores;

import com.example.noticia.entidades.Periodista;
import com.example.noticia.entidades.Usuario;
import com.example.noticia.repositorios.AdministradorRepositorio;
import com.example.noticia.repositorios.PeriodistaRepositorio;
import com.example.noticia.repositorios.UsuarioRepositorio;
import com.example.noticia.servicios.PeriodistaServicio;

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
@RequestMapping("/administrador")
public class AdministradorControlador {

    @Autowired
    private AdministradorRepositorio administradorRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;

    @Autowired
    private PeriodistaServicio periodistaServicio;

    @GetMapping("/listar/usuarios")
    public String listarUsuarios(ModelMap model) {

        List<Usuario> usuarios = usuarioRepositorio.buscarUsuarios();

        model.put("usuarios", usuarios);
        return "usuario_list.html";
    }

    @GetMapping("/listar/periodistas")
    public String listarPeriodistas(ModelMap model) {

        usuarioRepositorio.findAll();
        List<Periodista> periodistas = periodistaRepositorio.findAll();

        model.put("periodistas", periodistas);
        return "periodista_list.html";
    }

    @GetMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable String id, RedirectAttributes redirectAttributes) {

        periodistaServicio.cambiarEstado(id);
         redirectAttributes.addFlashAttribute("exito", "El estado se cambió con éxito.");
        return "redirect:/administrador/listar/periodistas";
    }

    @PostMapping("/cambiarRol")
    public String cambiarRolUsuario(@RequestParam String sueldoMensual, @RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            int sueldoMensual2 = Integer.valueOf(sueldoMensual);
            periodistaServicio.crearPeriodista(sueldoMensual2, id);
            redirectAttributes.addFlashAttribute("exito", "El usuario se modificó a periodista.");
            return "redirect:/administrador/listar/usuarios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Esa cantidad no es válida.");
            return "redirect:/administrador/listar/usuarios";
        }
    }
    
    @PostMapping("/modificarSueldo")
    public String modificarSueldo(@RequestParam String sueldoMensual, @RequestParam String id, RedirectAttributes redirectAttributes) {
        try {
            int sueldoMensual2 = Integer.valueOf(sueldoMensual);
            periodistaServicio.actualizarSueldo(sueldoMensual2, id);
            redirectAttributes.addFlashAttribute("exito", "El sueldo se modificó con éxito.");
            return "redirect:/administrador/listar/periodistas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Esa cantidad no es válida.");
            return "redirect:/administrador/listar/periodistas";
        }
    }
}
