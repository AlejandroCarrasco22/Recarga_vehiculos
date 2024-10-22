package com.uva.dbcs.sharedlibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uva.dbcs.sharedlibrary.dto.PuntoRecargaDTO;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PuntoRecargaService {

    private final WebClient webClient;

    public PuntoRecargaService(WebClient.Builder webClientBuilder) {
        String PUNTORECARGA_SERVICE_URL = System.getenv("PUNTORECARGA_SERVICE_URL") == null ?
                "http://chargerpoints:8080/paginaPrincipal/" : System.getenv("PUNTORECARGA_SERVICE_URL");
        this.webClient = webClientBuilder.baseUrl(PUNTORECARGA_SERVICE_URL).build();
    }

    // Obtiene los puntos de recarga por el Plug Type
    public List<PuntoRecargaDTO> getPuntoRecargaByPlugType(String plugType, String token){
        String response = this.webClient.get().uri("chargerpoints?plugType="+plugType)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        List<PuntoRecargaDTO> puntosRecargaDTO  = null;
        try{
            puntosRecargaDTO = objectMapper.readValue(response, new TypeReference<List<PuntoRecargaDTO>>() {});
        }catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }
        return puntosRecargaDTO;

    }

    public PuntoRecargaDTO getPuntoRecargaById(Integer id, String token){
        String response = this.webClient.get().uri("chargerpoints/"+id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve().bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        PuntoRecargaDTO puntoRecargaDTO = null;
        try {
            puntoRecargaDTO = objectMapper.readValue(response, new TypeReference<PuntoRecargaDTO>() {});
        } catch (JsonProcessingException e){
            System.out.println(e.getMessage());
        }

        return puntoRecargaDTO;
    }

    public void updateEstadoPuntoRecarga(PuntoRecargaDTO puntoRecargaDTO, String token){
        this.webClient.put().uri("/chargerpoints/"+puntoRecargaDTO.id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(puntoRecargaDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


}
