package com.uva.dbcs.chargerpoints.Controller;

import com.uva.dbcs.chargerpoints.Exception.PuntoRecargaException;
import com.uva.dbcs.chargerpoints.Repository.PuntoRecargaRepository;
import com.uva.dbcs.chargerpoints.Model.PuntoRecarga;
import com.uva.dbcs.chargerpoints.Model.Enums.PlugType;
import com.uva.dbcs.chargerpoints.Model.Enums.StatusType;

import net.minidev.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/paginaPrincipal")
@CrossOrigin(origins="*")
public class PuntoRecargaControlador {

    private final PuntoRecargaRepository puntoRecargaRepository;

    PuntoRecargaControlador(PuntoRecargaRepository puntoRecargaRepository){
        this.puntoRecargaRepository = puntoRecargaRepository;
    }
    
    // Método GET para obtener una lista con todos los puntos de recarga
    // Existe la opción de que en la URI se introduzca un parametro. En caso de que sea así, devuelve los puntos de carga con el plugType especificado
    @GetMapping(value = "/chargerpoints")
    public ArrayList<PuntoRecarga> getPuntosDeRecarga(@RequestParam (required = false) PlugType plugType) {
        if (plugType == null){
            return (ArrayList<PuntoRecarga>) puntoRecargaRepository.findAll();
        } else {
            return (ArrayList<PuntoRecarga>) puntoRecargaRepository.findByPlugType(plugType);
        }
    }

    // Método GET para obtener un punto de recarga concreto dado su id.
    @GetMapping(value = "/chargerpoints/{id}")
    public PuntoRecarga getPuntoDeRecargaPorId(@PathVariable Integer id){
        if(puntoRecargaRepository.findById(id).isEmpty()){
            throw new PuntoRecargaException("No hay registros de puntos de recargar con ese ID.");
        }

        return puntoRecargaRepository.findById(id).get();
    }

    // Método POST para crear un punto de carga
    @PostMapping(value="/chargerpoints", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newPuntoDeRecarga(@RequestBody PuntoRecarga newPuntoDeRecarga, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            puntoRecargaRepository.save(newPuntoDeRecarga);
            return ResponseEntity.ok("Nuevo registro de punto de recarga creado.");
        }
    }

    // Método PUT para modificar únicamente el estado de un punto de recarga
    @PutMapping(value = "/chargerpoints/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePuntoDeRecarga(@RequestBody @Valid JSONObject puntoRecarga,
                                                 @PathVariable Integer id, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            PuntoRecarga puntoPorActualizar = puntoRecargaRepository.findById(id).orElseThrow(()
                    -> new PuntoRecargaException("No se ha encontrado un punto de recarga con ese id."));
            String estado = String.valueOf(puntoRecarga.get("status"));
            switch (estado) {
                case "MANTENIMIENTO":
                    puntoPorActualizar.setStatus(StatusType.MANTENIMIENTO);
                    System.out.println(puntoPorActualizar.getStatus());
                    break;
                case "DISPONIBLE":
                    puntoPorActualizar.setStatus(StatusType.DISPONIBLE);
                    System.out.println(puntoPorActualizar.getStatus());
                    break;
                case "EN_SERVICIO":
                    puntoPorActualizar.setStatus(StatusType.EN_SERVICIO);
                    System.out.println(puntoPorActualizar.getStatus());
                    break;
            }
            
            puntoRecargaRepository.save(puntoPorActualizar);
            return ResponseEntity.ok("Punto de recarga actualizado correctamente.");
        }
    }

}
