package com.uva.dbcs.chargerpoints.Repository;

import com.uva.dbcs.chargerpoints.Model.PuntoRecarga;
import com.uva.dbcs.chargerpoints.Model.Enums.PlugType;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PuntoRecargaRepository extends JpaRepository<PuntoRecarga, Integer> {
    ArrayList<PuntoRecarga> findByPlugType(PlugType plugType);
}
