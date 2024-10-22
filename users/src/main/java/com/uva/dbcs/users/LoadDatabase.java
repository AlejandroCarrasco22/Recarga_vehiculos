package com.uva.dbcs.users;

import com.uva.dbcs.users.Model.Usuario;
import com.uva.dbcs.users.Repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository){

        return args -> {
            usuarioRepository.deleteAll();
            Usuario user1 = new Usuario("jor", "Jorge", "uva", "jor@uva.es", "jor", "12334");
            Usuario user2 = new Usuario("and", "Andres", "uva", "and@uva.es", "and", "12334");
            Usuario user3 = new Usuario("ale", "Alejandro", "uva", "ale@uva.es", "ale", "");
            log.info("Preloading " + usuarioRepository.save(user1));
            log.info("Preloading " + usuarioRepository.save(user2));
            log.info("Preloading " + usuarioRepository.save(user3));
        };
    }

}
