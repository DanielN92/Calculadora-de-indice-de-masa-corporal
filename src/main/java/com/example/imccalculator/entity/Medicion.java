package com.example.imccalculator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mediciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @NotNull(message = "El peso es requerido")
    @DecimalMin(value = "1.0", message = "El peso debe ser mayor a 1 kg")
    @DecimalMax(value = "500.0", message = "El peso debe ser menor a 500 kg")
    @Column(nullable = false)
    private Double peso;
    
    @NotNull(message = "El IMC es requerido")
    @Column(nullable = false)
    private Double imc;
    
    @NotBlank(message = "La categoría es requerida")
    @Column(nullable = false)
    private String categoria;
    
    public Medicion(User user, Double peso, Double imc, String categoria) {
        this.user = user;
        this.fecha = LocalDateTime.now();
        this.peso = peso;
        this.imc = imc;
        this.categoria = categoria;
    }
}
