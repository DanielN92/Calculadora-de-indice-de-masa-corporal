package com.example.imccalculator.controller;

import com.example.imccalculator.entity.User;
import com.example.imccalculator.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       @RequestParam(value = "registered", required = false) String registered,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }
        
        if (registered != null) {
            model.addAttribute("message", "Registro exitoso. Ahora puedes iniciar sesión");
        }
        
        return "login";
    }
    
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("user", new User());
        return "registro";
    }
    
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "registro";
        }
        
        try {
            userService.registrarUsuario(user);
            redirectAttributes.addAttribute("registered", "true");
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("nombreUsuario", "error.user", e.getMessage());
            return "registro";
        }
    }
}
