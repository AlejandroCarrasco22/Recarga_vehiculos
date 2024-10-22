package com.uva.dbcs.vehicles;

import com.uva.dbcs.vehicles.Model.Enums.PlugType;
import com.uva.dbcs.vehicles.Model.Vehiculo;
import com.uva.dbcs.vehicles.Repository.VehiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(VehiculoRepository vehiculoRepository){

        return args -> {
            vehiculoRepository.deleteAll();
        };
    }

}
