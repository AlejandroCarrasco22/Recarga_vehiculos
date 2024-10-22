package com.uva.dbcs.sharedlibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uva.dbcs.sharedlibrary.dto.UsuarioDTO;
import com.uva.dbcs.sharedlibrary.dto.VehiculoDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class VehiculoService {
    private final WebClient webClient;

    public VehiculoService(WebClient.Builder webClientBuilder) {
        String VEHICULO_SERVICE_URL = System.getenv("VEHICULO_SERVICE_URL") == null
                ? "http://vehicles:8083/paginaPrincipal/" : System.getenv("VEHICULO_SERVICE_URL");
        this.webClient = webClientBuilder.baseUrl(VEHICULO_SERVICE_URL).build();
    }

    public VehiculoDTO getVehiculoById(Integer id, String token){
        String response = this.webClient.get().uri("vehicles/"+id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        VehiculoDTO vehiculo = null;
        try{
            vehiculo = objectMapper.readValue(response, new TypeReference<VehiculoDTO>() {});
        } catch(JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return vehiculo;
    }

    public List<VehiculoDTO> getVehiclesByUserId(Integer userId, String token){
        // Busca todos los vehículos que estén asociados con el Usuario cuyo id ha sido
        // especificado por parámetro
        String response = this.webClient.get().uri("vehicles?userId="+userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        List<VehiculoDTO> vehiculosDeUsuario = null;
        try{
            vehiculosDeUsuario = objectMapper.readValue(response, new TypeReference<List<VehiculoDTO>>() {});
        }catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return vehiculosDeUsuario;
    }

    // Actualiza el vehiculo en la base de datos
    public void putVehiculo(VehiculoDTO vehiculo, String token){

        this.webClient.put().uri("/vehicles/"+vehiculo.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(vehiculo)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // Elimina el vehiculo de la BD.
    public void deleteVehiculoPorId(Integer idVehiculo, String token){
        this.webClient.delete().uri("/vehicles/"+idVehiculo)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
