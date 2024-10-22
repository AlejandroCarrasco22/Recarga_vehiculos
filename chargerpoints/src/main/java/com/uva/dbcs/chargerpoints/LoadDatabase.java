package com.uva.dbcs.chargerpoints;

import com.uva.dbcs.chargerpoints.Model.Enums.PlugType;
import com.uva.dbcs.chargerpoints.Model.Enums.PowerType;
import com.uva.dbcs.chargerpoints.Model.Enums.StatusType;
import com.uva.dbcs.chargerpoints.Model.PuntoRecarga;
import com.uva.dbcs.chargerpoints.Repository.PuntoRecargaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(PuntoRecargaRepository puntoRecargaRepository){

        return args -> {
            
            puntoRecargaRepository.deleteAll();
            PuntoRecarga puntoRecarga1 = new PuntoRecarga("Calle Paraiso", 12, 52, PlugType.CHADEMO, PowerType.ULTRARAPIDA, StatusType.DISPONIBLE);
            PuntoRecarga puntoRecarga2 = new PuntoRecarga("Calle Salvados", 34, 13, PlugType.MENNEKES, PowerType.LENTA, StatusType.DISPONIBLE);
            PuntoRecarga puntoRecarga3 = new PuntoRecarga("Plaza de la universidad", 90, 52, PlugType.SCHUKO, PowerType.RAPIDA, StatusType.EN_SERVICIO);
            log.info("Preloading " + puntoRecargaRepository.save(puntoRecarga1));
            log.info("Preloading " + puntoRecargaRepository.save(puntoRecarga2));
            log.info("Preloading " + puntoRecargaRepository.save(puntoRecarga3));
        };
    }

}
