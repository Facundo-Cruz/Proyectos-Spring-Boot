package com.egg.estancias.controladores;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Cliente;
import com.egg.estancias.entidades.Estancia;
import com.egg.estancias.servicios.CasaServicio;
import com.egg.estancias.servicios.ComentarioServicio;
import com.egg.estancias.servicios.EstanciaServicio;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reserva")
public class EstanciaControlador {

    @Autowired
    private EstanciaServicio estanciaServicio;

    @Autowired
    private CasaServicio casaServicio;
    
    @Autowired 
    private ComentarioServicio comentarioServicio;
    
    @GetMapping("/registrar/{idCasa}")
    public String registrar(@PathVariable String idCasa, ModelMap model) {
        Casa casa = casaServicio.casaPorId(idCasa);
        model.put("casa", casa);
        return "estancia_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String alias,@RequestParam String descripcion,
            @RequestParam String idCasa, @RequestParam String idCliente,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam String huesped, RedirectAttributes redirectAttributes) {
        try {
            estanciaServicio.crearEstancia(idCasa, idCliente, huesped, fechaDesde, fechaHasta);
            comentarioServicio.crearComentario(alias, idCasa, descripcion);
            redirectAttributes.addFlashAttribute("exito", "La reserva ha sido realizada correctamente.");
            return "redirect:/casa/inicio";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("huesped", huesped);
            return "redirect:/reserva/registrar/" + idCasa;
        }
    }
    
    @GetMapping("/listar")
    public String verCasa(ModelMap model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("usuariosession");
        String id = cliente.getId();
        List<Estancia> estancias = estanciaServicio.trearEstanciasPorCliente(id);
        if (estancias.isEmpty()) {
            model.put("vacio", "No tienes reservas cargadas en el sistema.");
        }
        model.put("estancias", estancias);
        return "estancia_list.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes){
        try {
            System.out.println(id);
            estanciaServicio.eliminarEstancia(id);
            
            redirectAttributes.addFlashAttribute("exito", "La reserva ha sido eliminada correctamente.");
            return "redirect:/casa/inicio";
        } catch (Exception e) {
            
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reserva/listar";
        }
        
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model){
        Estancia estancia = estanciaServicio.buscarPorId(id);
        model.put("estancia", estancia);
        return "estancia_modificar.html";
    }
    @PostMapping("/modificado")
    public String modificado(@RequestParam String id, @RequestParam String huesped, 
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta, 
            RedirectAttributes redirectAttributes
            ){
        try {
            estanciaServicio.modificarEstancia(id, huesped, fechaDesde, fechaHasta);
            redirectAttributes.addFlashAttribute("exito", "La reserva ha sido modificada correctamente.");
            return "redirect:/reserva/listar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reserva/modificar/"+id;
        }
    }
}
