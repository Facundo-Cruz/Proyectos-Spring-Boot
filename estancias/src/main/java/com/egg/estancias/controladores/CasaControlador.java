package com.egg.estancias.controladores;

import com.egg.estancias.entidades.Casa;
import com.egg.estancias.entidades.Familia;
import com.egg.estancias.servicios.CasaServicio;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/casa")
public class CasaControlador {

    @Autowired
    private CasaServicio casaServicio;

    @GetMapping("/inicio")
    public String inicio(ModelMap model) {
        List<Casa> casas = casaServicio.listarCasas();
        model.put("casas", casas);
        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "casa_form.html";
    }

    @GetMapping("/vista")
    public String verCasa(ModelMap model, HttpSession session) {
        Familia familia = (Familia) session.getAttribute("usuariosession");
        String idFamilia = familia.getId();
        List<Casa> casas = casaServicio.trarCasasDeFamilia(idFamilia);
        if (casas.isEmpty()) {
            model.put("vacio", "Tu familia no tiene casas cargadas en el sistema.");
        }
        model.put("casas", casas);
        return "casa_ver.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String idFamilia, @RequestParam String calle, @RequestParam int numero,
            @RequestParam String codPostal, @RequestParam String ciudad, @RequestParam String pais,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam int minDias, @RequestParam int maxDias, @RequestParam double precio,
            @RequestParam String tipoVivienda, RedirectAttributes redirectAttributes) {
        try {
            
            casaServicio.crearCasa(archivo, idFamilia, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
            redirectAttributes.addFlashAttribute("exito", "La casa ha sido cargada con éxito.");
            return "redirect:/casa/registrar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("calle", calle);
            redirectAttributes.addFlashAttribute("numero", numero);
            redirectAttributes.addFlashAttribute("codPostal", codPostal);
            redirectAttributes.addFlashAttribute("ciudad", ciudad);
            redirectAttributes.addFlashAttribute("pais", pais);
            redirectAttributes.addFlashAttribute("minDias", minDias);
            redirectAttributes.addFlashAttribute("maxDias", maxDias);
            redirectAttributes.addFlashAttribute("precio", precio);
            redirectAttributes.addFlashAttribute("tipoVivienda", tipoVivienda);
            return "redirect:/casa/registrar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            casaServicio.eliminarCasa(id);
            redirectAttributes.addFlashAttribute("exito", "La casa ha sido eliminada correctamente.");
            return "redirect:/casa/inicio";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/casa/vista";
        }
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap model) {
        
        Casa casa = casaServicio.casaPorId(id);
        model.put("casa", casa);
        return "casa_modificar.html";
    }

    @PostMapping("/modificado")
    public String modificar(MultipartFile archivo, @RequestParam String id, @RequestParam String calle, @RequestParam int numero,
            @RequestParam String codPostal, @RequestParam String ciudad, @RequestParam String pais,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDesde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaHasta,
            @RequestParam int minDias, @RequestParam int maxDias, @RequestParam double precio,
            @RequestParam String tipoVivienda, RedirectAttributes redirectAttributes) {
        try {
            casaServicio.modificarCasa(archivo, id, calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
            redirectAttributes.addFlashAttribute("exito", "La casa ha sido modificada correctamente.");
            return "redirect:/casa/modificar/"+id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/casa/modificar/"+id;
        }
    }
}
