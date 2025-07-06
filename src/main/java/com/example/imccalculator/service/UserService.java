package com.example.imccalculator.service;

import com.example.imccalculator.entity.User;
import com.example.imccalculator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User registrarUsuario(User user) {
        if (userRepository.existsByNombreUsuario(user.getNombreUsuario())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        
        // Encriptar contraseña
        user.setContraseña(passwordEncoder.encode(user.getContraseña()));
        
        return userRepository.save(user);
    }
    
    public Optional<User> buscarPorNombreUsuario(String nombreUsuario) {
        return userRepository.findByNombreUsuario(nombreUsuario);
    }
    
    public User actualizarPeso(User user, Double nuevoPeso) {
        user.setPeso(nuevoPeso);
        return userRepository.save(user);
    }
    
    public boolean existeNombreUsuario(String nombreUsuario) {
        return userRepository.existsByNombreUsuario(nombreUsuario);
    }
}
