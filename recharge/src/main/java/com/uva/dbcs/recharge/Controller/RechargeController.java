package com.uva.dbcs.recharge.Controller;


import com.uva.dbcs.recharge.Exception.RechargeException;
import com.uva.dbcs.recharge.Model.Recharge;
import com.uva.dbcs.recharge.Model.enums.Payment;
import com.uva.dbcs.recharge.Model.enums.Status;
import com.uva.dbcs.recharge.Repository.RechargeRepository;
import com.uva.dbcs.sharedlibrary.dto.PuntoRecargaDTO;
import com.uva.dbcs.sharedlibrary.dto.UsuarioDTO;
import com.uva.dbcs.sharedlibrary.dto.VehiculoDTO;
import com.uva.dbcs.sharedlibrary.service.PuntoRecargaService;
import com.uva.dbcs.sharedlibrary.service.UsuarioService;
import com.uva.dbcs.sharedlibrary.service.VehiculoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paginaPrincipal")
@CrossOrigin(origins="*")
public class RechargeController {

    private final RechargeRepository rechargeRepository;
    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;
    private final PuntoRecargaService puntoRecargaService;

    // Los precios de cada tipo de cargador que se definen en el enunciado;
    private final double PRECIO_LENTA = 0.13;
    private final double PRECIO_MEDIA = 0.20;
    private final double PRECIO_RAPIDA = 0.35;
    private final double PRECIO_ULTRARRAPIDA = 0.5;

    public RechargeController(RechargeRepository rechargeRepository, UsuarioService usuarioService, VehiculoService vehiculoService,
                              PuntoRecargaService puntoRecargaService){
        this.rechargeRepository = rechargeRepository;
        this.usuarioService = usuarioService;
        this.vehiculoService = vehiculoService;
        this.puntoRecargaService = puntoRecargaService;
    }

    // Método GET para obtener las recargas de un usuario ordenadas de forma descendente según
    // su fecha de comienzo.
    @GetMapping(value = "/recharge")
    public List<Recharge> getRecargasDeUsuario (@RequestParam Integer userId){
        List<Recharge> recargasUsuario = rechargeRepository.findByUserId(userId);

        // Ordenamos las recargas de usuario de forma descendente
        // en función de su "dateStart", tal y como se nos pide en el enunciado.
        // Contemplamos también los casos en los que las fechas no estén aún indicadas
        // (que sean null porque la recarga aun no ha empezado).
        recargasUsuario.sort((r1, r2) -> {
            Date dateStartA = r1.getDateStart();
            Date dateStartB = r2.getDateStart();

            if (dateStartA == null && dateStartB == null) {
                return 0; // Ambos son nulos, son considerados iguales.
            } else if (dateStartA == null) {
                return 1; // dateStartA es nulo, se coloca al final.
            } else if (dateStartB == null) {
                return -1; // dateStartB es nulo, se coloca al final.
            } else {
                return dateStartB.compareTo(dateStartA); // Orden descendente para fechas no nulas.
            }
        });

        return recargasUsuario;
    }

    // Método GET para obtener una recarga dado su id.
    @GetMapping(value = "/recharge/{id}")
    public Recharge getRecargaPorId (@PathVariable Integer id){
        if(rechargeRepository.findById(id).isEmpty()){
            throw new RechargeException("No se han encontrado registros de recargas con ese ID.");
        }

        return rechargeRepository.findById(id).get();
    }

    // Método POST para crear una nueva recarga.
    @PostMapping(value = "/recharge", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> crearRecarga(@RequestBody @Valid Recharge newRecharge, BindingResult bindingResult,
                                               @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            String token = extraerToken(authorizationHeader);

            try{
                // Comprobamos que los IDs del Vehiculo, el Usuario y el Punto de carga correspondan a instancias existentes
                // de cada clase.
                UsuarioDTO usuarioRecarga = usuarioService.getUserByUserId(newRecharge.getUserId(), token);
                if(usuarioRecarga == null){
                    throw new RechargeException("No se han encontrado usuarios con ese ID");
                }

                VehiculoDTO vehiculoRecarga = vehiculoService.getVehiculoById(newRecharge.getVehicleId(), token);
                if(vehiculoRecarga == null){
                    throw new RechargeException("No se han encontrado vehículos con ese ID");
                }

                PuntoRecargaDTO puntoCargaAsignado = puntoRecargaService.getPuntoRecargaById(newRecharge.getChargerpointId(), token);
                if(puntoCargaAsignado == null){
                    throw new RechargeException("No se han encontrado vehículos con ese ID");
                }

                // Si el usuario no tiene método de pago asociado no podrá realizar la recarga.
                if(usuarioRecarga.paymentCard.isEmpty() || usuarioRecarga.paymentCard == null){
                    throw new RechargeException("El usuario especificado no tiene un método de pago asignado.");
                }

                // Si el usuario asociado no es el dueño del vehículo tampoco podrá realizar la recarga.
                if(!Objects.equals(usuarioRecarga.id, vehiculoRecarga.userId)){
                    throw new RechargeException("El usuario no es el dueño del vehículo especificado");
                }


                // Calculamos el precio de la recarga (€/kwh) en función el tipo de cargador que se ha asignado.
                switch(puntoCargaAsignado.power){
                    case("LENTA"):
                        newRecharge.setPrice(PRECIO_LENTA);
                        break;
                    case("MEDIA"):
                        newRecharge.setPrice(PRECIO_MEDIA);
                        break;
                    case("RAPIDA"):
                        newRecharge.setPrice(PRECIO_RAPIDA);
                        break;
                    case("ULTRARAPIDA"):
                        newRecharge.setPrice(PRECIO_ULTRARRAPIDA);
                        break;
                }

                // Guardamos la recarga en la BD.
                rechargeRepository.save(newRecharge);
            } catch(RechargeException e){
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Error al crear la recarga: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al procesar la solicitud: " + e.getMessage());
            }

            return ResponseEntity.ok("Nuevo registro de recarga creado.");
        }
    }


    // Método PUT para actualizar los datos y el estado de la recarga especificada en función de la acción
    // que se quiera llevar a cabo sobre la recarga, la cual puede ser "START" (si el usuario confirma
    // que ha conectado el vehículo), "STOP" (si el usuario confirma que la recarga ha finalizado)
    // o "CANCEL" (si el usuario cancela antes de comenzar la recarga). Esta accion es pasada como parámetro.
    @PutMapping(value = "/recharge/{rechargeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRecarga (@PathVariable Integer rechargeId, @RequestBody Recharge recharge,
                                                 @RequestParam String accion, BindingResult bindingResult,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws NoSuchAlgorithmException{
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            String token = extraerToken(authorizationHeader);

            Recharge recarga = rechargeRepository.findById(rechargeId).orElseThrow(()
                        -> new RechargeException("No se han encontrado registros de recargas con ese ID."));

            PuntoRecargaDTO puntoRecarga = puntoRecargaService.getPuntoRecargaById(recarga.getChargerpointId(), token);

            try {
                // En función de la acción llevamos a cabo un procedimiento u otro.
                switch (accion) {
                    // La acción "START" representa la conexión del vehículo al punto de carga. En este caso,
                    // la fecha de comienzo de la recarga pasa a ser la actual, el estado de la carga pasa a ser "CHARGING" y el
                    // estado del pago a "PAYMENT". Además, el punto de carga que se está utilizando pasa a estar ocupado, es decir,
                    // en estado "EN_SERVICIO" . Antes de ello, comprobamos que el punto de carga no esté ocupado (que no esté ni en
                    // mantenimiento ni en servicio).
                    case ("START"):
                        if (!puntoRecarga.status.equals("DISPONIBLE")) {
                            throw new RechargeException("El punto de carga está ocupado en este momento.");
                        }

                        recarga.setDateStart(new Date());
                        recarga.setStatus(Status.CHARGING);
                        recarga.setPayment(Payment.PENDING);

                        // Actualizamos el estado del punto de carga a en servicio;
                        puntoRecarga.status = "EN_SERVICIO";
                        puntoRecargaService.updateEstadoPuntoRecarga(puntoRecarga, token);
                        break;

                    // Por otro lado, si la acción con la que se ha llamado a este método PUT es "STOP",
                    // significa que la recarga ha finalizado, por ello pasara a estado "COMPLETED", el pago (como se gestiona automaticamente)
                    // pasa también directamente a "COMPLETED", el número de kilovatios cargados lo calculamos como un número aleatorio entre 1
                    // y la capacidad máxima del vehículo, y actualizamos la fecha de finalización a la actual. Además, el estado del punto de carga
                    // pasa a ser "DISPONIBLE" porque ya ha finalizado la recarga.
                    case ("STOP"):
                        recarga.setDateEnd(new Date());
                        recarga.setStatus(Status.COMPLETED);
                        recarga.setPayment(Payment.COMPLETED);

                        // Obtenemos los kW máximos de capacidad que tiene el vehículo.
                        double capacidadMaxVehiculo = vehiculoService.getVehiculoById(recarga.getVehicleId(), token).capacity;
                        // Calculamos los kW cargados como un número aleatorio entre 1 y la capacidad máxima.
                        double kilovatiosCargados = 1 + (capacidadMaxVehiculo - 1) * new Random().nextDouble();
                        // Math.round junto con una multiplicación por 100 y división por lo mismo para quedarnos
                        // solo con los dos primeros decimales.
                        recarga.setKw(Math.round(kilovatiosCargados * 100.0) / 100.0);

                        puntoRecarga.status = "DISPONIBLE";
                        puntoRecargaService.updateEstadoPuntoRecarga(puntoRecarga, token);
                        break;

                    // Si la acción es "CANCEL", significa que no se ha llegado a realizar la recarga. En este caso
                    // el estado de la recarga se queda en "NotStarted" y el pago en "Cancelled".
                    case ("CANCEL"):
                        recarga.setStatus(Status.NOTSTARTED);
                        recarga.setPayment(Payment.CANCELLED);
                        break;

                    // En el caso de que la acción especificada no sea ninguna de las esperadas, se interpreta
                    // como una "bad request".
                    default:
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                "Acción no válida (debe ser START, STOP o CANCEL): " + accion);
                }
            } catch (RechargeException e){
                // En el caso en el que se quiera empezar una recarga en un punto que actualmente está en servicio.
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Error al actualizar la recarga: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al procesar la solicitud: " + e.getMessage());
            }

            try{
                rechargeRepository.save(recarga);
            } catch (Exception e){
                throw new RechargeException("Error al actualizar la recarga.");
            }

            return ResponseEntity.ok("Recarga actualizada correctamente");
        }
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
