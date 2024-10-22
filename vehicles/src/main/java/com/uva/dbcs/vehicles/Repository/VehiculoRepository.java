package com.uva.dbcs.vehicles.Repository;

import com.uva.dbcs.vehicles.Model.Vehiculo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    List<Vehiculo> findByUserId(Integer userId);
}
