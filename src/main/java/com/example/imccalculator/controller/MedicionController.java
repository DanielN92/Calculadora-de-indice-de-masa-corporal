package com.example.imccalculator.controller;

import com.example.imccalculator.entity.Medicion;
import com.example.imccalculator.entity.User;
import com.example.imccalculator.service.MedicionService;
import com.example.imccalculator.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MedicionController {
    
    private final UserService userService;
    private final MedicionService medicionService;
    
    @GetMapping("/nueva-medicion")
    public String nuevaMedicion(Authentication authentication, Model model) {
        String nombreUsuario = authentication.getName();
        Optional<User> userOpt = userService.buscarPorNombreUsuario(nombreUsuario);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
        }
        
        return "nueva-medicion";
    }
    
    @PostMapping("/calcular-imc")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calcularIMC(@RequestParam Double peso,
                                                          Authentication authentication) {
        try {
            log.info("Calculando IMC para peso: {}", peso);
            
            String nombreUsuario = authentication.getName();
            Optional<User> userOpt = userService.buscarPorNombreUsuario(nombreUsuario);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                MedicionService.IMCResult result = medicionService.calcularIMC(peso, user.getEstatura());
                
                Map<String, Object> response = new HashMap<>();
                response.put("imc", result.getImc());
                response.put("categoria", result.getCategoria());
                response.put("success", true);
                
                log.info("IMC calculado: {} - Categoría: {}", result.getImc(), result.getCategoria());
                
                return ResponseEntity.ok(response);
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Usuario no encontrado");
            
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            log.error("Error al calcular IMC: ", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Error interno del servidor");
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PostMapping("/guardar-medicion")
    public String guardarMedicion(@RequestParam Double peso,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        
        try {
            log.info("Guardando medición con peso: {}", peso);
            
            String nombreUsuario = authentication.getName();
            Optional<User> userOpt = userService.buscarPorNombreUsuario(nombreUsuario);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Guardar medición
                Medicion medicion = medicionService.guardarMedicion(user, peso);
                log.info("Medición guardada con ID: {}", medicion.getId());
                
                // Actualizar peso del usuario
                userService.actualizarPeso(user, peso);
                log.info("Peso del usuario actualizado a: {}", peso);
                
                redirectAttributes.addFlashAttribute("message", "Medición guardada correctamente");
                redirectAttributes.addFlashAttribute("messageType", "success");
                
                return "redirect:/home";
            }
            
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            redirectAttributes.addFlashAttribute("messageType", "error");
            
        } catch (Exception e) {
            log.error("Error al guardar medición: ", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar la medición: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        
        return "redirect:/nueva-medicion";
    }
    
    @GetMapping("/historial")
    public String historial(Authentication authentication, Model model) {
        String nombreUsuario = authentication.getName();
        Optional<User> userOpt = userService.buscarPorNombreUsuario(nombreUsuario);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Medicion> mediciones = medicionService.obtenerMedicionesPorUsuario(user);
            
            model.addAttribute("user", user);
            model.addAttribute("mediciones", mediciones);
            model.addAttribute("totalMediciones", mediciones.size());
            
            if (!mediciones.isEmpty()) {
                model.addAttribute("pesoActual", mediciones.get(0).getPeso());
                model.addAttribute("imcActual", mediciones.get(0).getImc());
            }
        }
        
        return "historial";
    }
}
