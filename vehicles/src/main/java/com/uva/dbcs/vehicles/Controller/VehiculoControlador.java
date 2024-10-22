package com.uva.dbcs.vehicles.Controller;


import com.uva.dbcs.sharedlibrary.dto.PuntoRecargaDTO;
import com.uva.dbcs.sharedlibrary.dto.UsuarioDTO;
import com.uva.dbcs.sharedlibrary.service.PuntoRecargaService;
import com.uva.dbcs.sharedlibrary.service.UsuarioService;
import com.uva.dbcs.vehicles.Exception.UsuarioException;
import com.uva.dbcs.vehicles.Exception.VehiculoException;
import com.uva.dbcs.vehicles.Model.Vehiculo;
import com.uva.dbcs.vehicles.Repository.VehiculoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/paginaPrincipal")
@CrossOrigin(origins="*")
public class VehiculoControlador {
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioService usuarioService;
    private final PuntoRecargaService puntoRecargaService;

    VehiculoControlador(VehiculoRepository vehiculoRepository,
                        UsuarioService usuarioService, PuntoRecargaService puntoRecargaService){
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioService = usuarioService;
        this.puntoRecargaService = puntoRecargaService;
    }

    
    // Método GET para obtener la información de todos los vehículos guardados en la BD.
    //Devuelve todos los vehiculos si no manda id, y el de un usuario en concreto si manda el id de usuario
    @GetMapping(value = "/vehicles")
    public List<Vehiculo> getVehiculosDeUsuario(@RequestParam (required = false) Integer userId){
        if (userId == null){
            return vehiculoRepository.findAll();
        } else {
            return vehiculoRepository.findByUserId(userId);
        }
    }


    // Método GET para obtener toda la información de un vehículo a partir de su id.
    @GetMapping("/vehicles/{id}")
    public Vehiculo getVehiculoById(@PathVariable Integer id){

        if(vehiculoRepository.findById(id).isEmpty()) {
            throw new VehiculoException("No hay registros de vehiculos con ese id");
        }else {
            return vehiculoRepository.findById(id).get();
        }
    }


    // Método POST para crear un nuevo vehiculo.
    // En caso de que el usuario al que este asociado tenga un numero de tarjeta, automaticamente su estado debe pasar a activo.
    @PostMapping(value = "/vehicles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newVehiculo(@RequestBody @Valid Vehiculo nuevoVehiculo, BindingResult bindingResult,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            try{
                // Extraemos el token de la cabecera AUTHORIZATION.
                String token = extraerToken(authorizationHeader);

                // Comprobamos que el usuario exista en la BD.
                if(usuarioService.getUserByUserId(nuevoVehiculo.getUserId(), token) == null){
                    throw new VehiculoException("No se han encontrado usuarios con ese ID");
                }

                // Si la matrícula no tiene 7 caracteres se considera como un error.
                if(nuevoVehiculo.getCarRegistration().length() != 7){
                    throw new VehiculoException("El formato de la matrícula es incorrecrto (debe tener 7 caracteres)");
                }

                vehiculoRepository.save(nuevoVehiculo);

                // Busca el usuario al que está asociado el vehiculo creado y comprueba que tiene tarjeta de credito, en
                // cuyo caso se cambiará el estado del usuario a true.
                UsuarioDTO usuarioDTO = usuarioService.getUserByUserId(nuevoVehiculo.getUserId(), token);
                if (usuarioDTO != null) {
                    // Si el usuario tiene tarjeta de pago, al haberle añadido ahora un vehiculo,
                    // el usuario pasa a estar "activado".
                    if (usuarioDTO.paymentCard != null && !usuarioDTO.paymentCard.isEmpty()) {
                        usuarioDTO.enabled = true;
                        try {
                            usuarioService.putUsuario(usuarioDTO, token);
                        } catch (Exception e){
                            throw new UsuarioException("Error al actualizar el usuario");
                        }
                    }
                }
                else{
                    throw new VehiculoException("No se ha encontrado un usuario con el id especificado");
                }

            } catch (VehiculoException e) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Error al crear el vehículo: " + e.getMessage());
            } catch (UsuarioException e) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Error al actualizar el usuario: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al procesar la solicitud: " + e.getMessage());
            }

            return ResponseEntity.ok("Nuevo registro de vehículo creado.");
        }

    }
    

    // Método DELETE para eliminar un vehiculo, especificado por su id.
    // En caso de que sea el ultimo vehiculo asociado a un usuario, el estado del mismo pasa a estar desactivado
    @DeleteMapping(value = "/vehicles/{id}")
    public ResponseEntity<String> deleteVehiculo(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        try{
            String token = extraerToken(authorizationHeader);

            Vehiculo vehiculoAEliminar = vehiculoRepository.findById(id).orElse(null);

            if (vehiculoAEliminar != null) {
                UsuarioDTO usuario = usuarioService.getUserByUserId(vehiculoAEliminar.getUserId(), token);
                // Lo normal es que la primera condición del siguiente AND ea siempre true,
                // pues no se puede crear un vehiculo sin asociarle un id de un usuario existente.
                if(usuario != null && vehiculoRepository.findByUserId(usuario.id).size() == 1){
                    // Si el usuario dueño del vehículo a punto de ser eliminado solo tiene
                    // un solo vehículo asociado (es decir, el que va a ser borrado), su estado
                    // pasará a ser false.
                    usuario.enabled = false;
                    usuarioService.putUsuario(usuario, token);
                }

            }
            vehiculoRepository.deleteById(id);
            return ResponseEntity.ok("Registro de vehiculo borrado");
        } catch (Exception e){
            throw new VehiculoException("Identificador de vehiculo no encontrado");
        }
    }

    // Método GET para obtener la lista de los puntos de recarga con el mismo plugType que el vehiculo seleccionado
    @GetMapping("/vehicles/{id}/chargerpoints")
    public List<PuntoRecargaDTO> getPuntoDeRecargaByPlugTypeCompatible(@PathVariable Integer id,
                                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String token = extraerToken(authorizationHeader);

        Optional<Vehiculo> vehiculoOptional = vehiculoRepository.findById(id);

        if (vehiculoOptional.isEmpty()) {
           throw new UsuarioException("No existe ningún vehículo con ese id");
        } else {
            Vehiculo vehiculo = vehiculoOptional.get();
            return puntoRecargaService.getPuntoRecargaByPlugType(vehiculo.getPlugType().toString(), token);
        }
    }

    // Método GET para obtener la lista de vehiculos asociados a un usuario
    @GetMapping("/users/{id}/vehicles")
    public List<Vehiculo> getVehiculoByUsuario(@PathVariable Integer id) {
        return vehiculoRepository.findByUserId(id);
    }


    // Método para extraer el token de una cabecera AUTHORIZATION de tipo Bearer token.
    private String extraerToken(String authorizationHeader){
        // Comprueba que el encabezado no sea nulo y comience con "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrae el token eliminando "Bearer " del encabezado
            return authorizationHeader.substring(7);
        }
        // En caso de que el encabezado no tenga el formato esperado ("Bearer $(token)"), pero
        // lo normal es que lo tenga.
        throw new RuntimeException("Formato de encabezado Authorization no válido");
    }

}
