package com.example.imccalculator.service;

import com.example.imccalculator.entity.Medicion;
import com.example.imccalculator.entity.User;
import com.example.imccalculator.repository.MedicionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicionService {
    
    private final MedicionRepository medicionRepository;
    
    public Medicion guardarMedicion(User user, Double peso) {
        // Calcular IMC
        double imc = peso / (user.getEstatura() * user.getEstatura());
        imc = Math.round(imc * 100.0) / 100.0;
        
        // Determinar categoría
        String categoria = determinarCategoria(imc);
        
        // Crear y guardar medición
        Medicion medicion = new Medicion(user, peso, imc, categoria);
        return medicionRepository.save(medicion);
    }
    
    public List<Medicion> obtenerMedicionesPorUsuario(User user) {
        return medicionRepository.findByUserOrderByFechaDesc(user);
    }
    
    public Optional<Medicion> obtenerUltimaMedicion(User user) {
        return medicionRepository.findLatestByUser(user);
    }
    
    public long contarMedicionesPorUsuario(User user) {
        return medicionRepository.countByUser(user);
    }
    
    public IMCResult calcularIMC(Double peso, Double estatura) {
        double imc = peso / (estatura * estatura);
        imc = Math.round(imc * 100.0) / 100.0;
        String categoria = determinarCategoria(imc);
        
        return new IMCResult(imc, categoria);
    }
    
    private String determinarCategoria(double imc) {
        if (imc < 18.5) {
            return "Bajo peso";
        } else if (imc >= 18.5 && imc < 25) {
            return "Peso normal";
        } else if (imc >= 25 && imc < 30) {
            return "Sobrepeso";
        } else if (imc >= 30 && imc < 35) {
            return "Obesidad grado I";
        } else if (imc >= 35 && imc < 40) {
            return "Obesidad grado II";
        } else {
            return "Obesidad grado III";
        }
    }
    
    public static class IMCResult {
        private final double imc;
        private final String categoria;
        
        public IMCResult(double imc, String categoria) {
            this.imc = imc;
            this.categoria = categoria;
        }
        
        public double getImc() { return imc; }
        public String getCategoria() { return categoria; }
    }
}
