package com.example.imccalculator.controller;

import com.example.imccalculator.entity.Medicion;
import com.example.imccalculator.entity.User;
import com.example.imccalculator.service.MedicionService;
import com.example.imccalculator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final UserService userService;
    private final MedicionService medicionService;
    
    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String nombreUsuario = authentication.getName();
        Optional<User> userOpt = userService.buscarPorNombreUsuario(nombreUsuario);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            
            // Calcular IMC actual
            MedicionService.IMCResult imcActual = medicionService.calcularIMC(user.getPeso(), user.getEstatura());
            model.addAttribute("imcActual", imcActual);
            
            // Obtener última medición
            Optional<Medicion> ultimaMedicion = medicionService.obtenerUltimaMedicion(user);
            ultimaMedicion.ifPresent(medicion -> model.addAttribute("ultimaMedicion", medicion));
            
            // Contar total de mediciones
            long totalMediciones = medicionService.contarMedicionesPorUsuario(user);
            model.addAttribute("totalMediciones", totalMediciones);
        }
        
        return "home";
    }
}
