package com.egg.estancias.controladores;

import com.egg.estancias.servicios.ClienteServicio;
import com.egg.estancias.servicios.FamiliaServicio;
import com.egg.estancias.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private FamiliaServicio familiaServicio;

    @GetMapping("/")
    public String index() {
        return "redirect:/casa/inicio";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "¡Usuario o contraseña invalidos!");
        }
        return "login.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String alias,
            @RequestParam String clave, @RequestParam String clave2, @RequestParam String rol,
            @RequestParam(required = false) String calle, @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String codPostal, @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String pais, @RequestParam(required = false) Integer numHijos,
            @RequestParam(required = false) Integer edadMin, @RequestParam(required = false) Integer edadMax,
            ModelMap model, RedirectAttributes redirectAttributes) {

        try {

            if (rol.equalsIgnoreCase("familia")) {
                
                familiaServicio.crearFamilia(alias, email, clave, clave2, edadMin, edadMax, numHijos);
                
            } else if (rol.equalsIgnoreCase("cliente")) {
                
                clienteServicio.crearCliente(alias, email, clave, clave2, calle, numero, codPostal, ciudad, pais);
                
            } else {

                usuarioServicio.crearUsuario(alias, email, clave, clave2);
            }
            redirectAttributes.addFlashAttribute("exito", "¡Has sido registrado con éxito!");
            return "redirect:/login";
        } catch (Exception ex) {
            model.put("error", ex.getMessage());
            model.put("email", email);
            model.put("alias", alias);
            model.put("rol", rol);
            model.put("calle", calle);
//            model.put("numero", numero);
//            model.put("codPostal", codPostal);
//            model.put("ciudad", ciudad);
//            model.put("pais", pais);

            return "registrar.html";
        }

    }
}
