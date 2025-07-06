package com.example.imccalculator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre completo es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;
    
    @NotBlank(message = "El nombre de usuario es requerido")
    @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String contraseña;
    
    @NotNull(message = "La edad es requerida")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad debe ser menor a 120")
    @Column(nullable = false)
    private Integer edad;
    
    @NotBlank(message = "El sexo es requerido")
    @Pattern(regexp = "masculino|femenino", message = "El sexo debe ser masculino o femenino")
    @Column(nullable = false)
    private String sexo;
    
    @NotNull(message = "La estatura es requerida")
    @DecimalMin(value = "0.5", message = "La estatura debe ser mayor a 0.5 metros")
    @DecimalMax(value = "3.0", message = "La estatura debe ser menor a 3 metros")
    @Column(nullable = false)
    private Double estatura;
    
    @NotNull(message = "El peso es requerido")
    @DecimalMin(value = "1.0", message = "El peso debe ser mayor a 1 kg")
    @DecimalMax(value = "500.0", message = "El peso debe ser menor a 500 kg")
    @Column(nullable = false)
    private Double peso;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Medicion> mediciones;
    
    // Constructor sin mediciones
    public User(String nombreCompleto, String nombreUsuario, String contraseña, 
                Integer edad, String sexo, Double estatura, Double peso) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.edad = edad;
        this.sexo = sexo;
        this.estatura = estatura;
        this.peso = peso;
    }
}
