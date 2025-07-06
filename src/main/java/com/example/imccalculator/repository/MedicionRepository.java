package com.example.imccalculator.repository;

import com.example.imccalculator.entity.Medicion;
import com.example.imccalculator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicionRepository extends JpaRepository<Medicion, Long> {
    
    List<Medicion> findByUserOrderByFechaDesc(User user);
    
    @Query("SELECT m FROM Medicion m WHERE m.user = :user ORDER BY m.fecha DESC")
    List<Medicion> findLatestByUserOrderByFecha(@Param("user") User user);
    
    default Optional<Medicion> findLatestByUser(User user) {
        List<Medicion> mediciones = findLatestByUserOrderByFecha(user);
        return mediciones.isEmpty() ? Optional.empty() : Optional.of(mediciones.get(0));
    }
    
    long countByUser(User user);
}
