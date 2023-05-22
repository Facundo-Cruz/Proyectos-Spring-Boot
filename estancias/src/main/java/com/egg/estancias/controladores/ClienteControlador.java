package com.egg.estancias.controladores;

import com.egg.estancias.entidades.Cliente;
import com.egg.estancias.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {
    
    @Autowired 
    private ClienteServicio clienteServicio;
    
    @GetMapping("/lista")
    public String listar(ModelMap model){
        List<Cliente> clientes = clienteServicio.listarClientes();
        model.put("clientes", clientes);
        return "clientes_list.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes){
        try {
            clienteServicio.eliminarCliente(id);
            redirectAttributes.addFlashAttribute("exito", "El cliente ha sido eliminado correctamente.");
            return "redirect:/cliente/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente/lista";
        }
    }
    
    @GetMapping("/darBaja/{id}/{estado}")
    public String darBaja(@PathVariable String id, @PathVariable Boolean estado, RedirectAttributes redirectAttributes) {
        try {

            if (estado) {
                clienteServicio.darBajaUsuario(id);
                redirectAttributes.addFlashAttribute("exito", "El cliente ha sido dado de baja correctamente.");
            } else {
                clienteServicio.darAltaCliente(id);
                redirectAttributes.addFlashAttribute("exito", "El cliente ha sido dado de alta correctamente.");
            }

            return "redirect:/cliente/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente/lista";
        }
    }
}
