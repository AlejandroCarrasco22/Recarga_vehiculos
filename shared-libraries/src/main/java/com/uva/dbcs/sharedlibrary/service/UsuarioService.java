package com.uva.dbcs.sharedlibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import com.uva.dbcs.sharedlibrary.dto.UsuarioDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UsuarioService {

    private final WebClient webClient;

    public UsuarioService(WebClient.Builder webClientBuilder) {
        String USUARIO_SERVICE_URL = System.getenv("USUARIO_SERVICE_URL") == null ?
                "http://users:8082/paginaPrincipal/" : System.getenv("USUARIO_SERVICE_URL");
        this.webClient = webClientBuilder.baseUrl(USUARIO_SERVICE_URL).build();
    }

    // Obtiene un usario dado un id (y el token JWT que nos permita hacer la llamada GET al endpoint
    // correspondiente en "users".
    public UsuarioDTO getUserByUserId(Integer userId, String token){

        String response = this.webClient.get().uri("users/"+userId)
                // Añadimos en la cabecera el token pasado por parámetro.
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        UsuarioDTO usuarioDTO = null;
        try{
            usuarioDTO = objectMapper.readValue(response, new TypeReference<UsuarioDTO>() {});
        }catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return usuarioDTO;
    }

    public UsuarioDTO getUserByEmail(String email){
        String response = this.webClient.get().uri("users?email="+email)
                .retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        UsuarioDTO usuarioDTO = null;
        try{
            // Obtenemos la lista de usuarios con ese email, pero como el email es único, la lista tendrá solo
            // un usuario, así que en el caso de que la lista no esté vacía, será ese único usuario que contiene
            // el que nos interesa.
            List<UsuarioDTO> listaUsuariosDTO = objectMapper.readValue(response, new TypeReference<List<UsuarioDTO>>() {});
            if(!listaUsuariosDTO.isEmpty()){
                usuarioDTO = listaUsuariosDTO.get(0);
            }

        }catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return usuarioDTO;
    }

    public UsuarioDTO getUserByPassword(String password){
        String response = this.webClient.get().uri("users?password="+password)
                .retrieve().bodyToMono(String.class).block();
        ObjectMapper objectMapper = new ObjectMapper();
        UsuarioDTO usuarioDTO = null;
        try{
            List<UsuarioDTO> listaUsuariosDTO = objectMapper.readValue(response, new TypeReference<List<UsuarioDTO>>() {});
            if(!listaUsuariosDTO.isEmpty()){
                usuarioDTO = listaUsuariosDTO.get(0);
            }
        }catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return usuarioDTO;
    }

    // Actualiza el usuario en la base de datos
    public void putUsuario(UsuarioDTO usuarioDTO, String token){

        this.webClient.put().uri("/users/"+usuarioDTO.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(usuarioDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
