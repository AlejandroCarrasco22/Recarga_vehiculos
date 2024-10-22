package com.uva.dbcs.users.Controller;

import com.uva.dbcs.sharedlibrary.dto.VehiculoDTO;
import com.uva.dbcs.sharedlibrary.service.VehiculoService;
import com.uva.dbcs.users.Exception.UsuarioException;
import com.uva.dbcs.users.Model.Usuario;
import com.uva.dbcs.users.Repository.UsuarioRepository;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paginaPrincipal")
@CrossOrigin(origins="*")
public class UsuarioControlador {

    private final UsuarioRepository usuarioRepository;
    private final VehiculoService vehiculoService;

    UsuarioControlador(UsuarioRepository usuarioRepository, VehiculoService vehiculoService){
        this.usuarioRepository = usuarioRepository;
        this.vehiculoService = vehiculoService;
    }

    // Método GET para obtener una lista con todos los usuarios. Existe la opción de que en la URI se introduzca un parámetro.
    // En ese caso sólo se mostrará la lista de los usuarios dependiendo del valor introducido.
    @GetMapping(value = "/users")
    public ArrayList<Usuario> getUsuarios(@RequestParam (required = false) Boolean enabled,
                                          @RequestParam (required = false) String email,
                                          @RequestParam (required = false) String password) throws NoSuchAlgorithmException {
        if (enabled == null && email == null && password == null){
            return (ArrayList<Usuario>) usuarioRepository.findAll();
        } else if(email == null && password == null) {
            return (ArrayList<Usuario>) usuarioRepository.findByEnabled(enabled);
        }else if (password == null) {
            return (ArrayList<Usuario>) usuarioRepository.findByEmail(email);
        }else {
            // Se hashea la contraseña para realizar al comprobación en la base de datos, ya que no está almacenado en texto plano
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            String passwordHash = Base64.getEncoder().encodeToString(hash);
            return (ArrayList<Usuario>) usuarioRepository.findByPassword(passwordHash);
        }
    }

    // Método GET para obtener un usuario concreto dado su id.
    @GetMapping(value = "/users/{id}")
    public Usuario getUsuarioPorId(@PathVariable Integer id){
        if(usuarioRepository.findById(id).isEmpty()){
            throw new UsuarioException("No hay registros de usuario con ese ID.");
        }

        return usuarioRepository.findById(id).get();
    }

    // Método POST para crear un nuevo usuario
    @PostMapping(value="/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newUsuario(@RequestBody @Valid Usuario newUsuario, BindingResult bindingResult) throws NoSuchAlgorithmException{
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            // Se hashea la contraseña para que no aparezca como texto plano en la base de datos
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(newUsuario.getPassword().getBytes());
            String passwordHash = Base64.getEncoder().encodeToString(hash);
            newUsuario.setPassword(passwordHash);
            usuarioRepository.save(newUsuario);
            return ResponseEntity.ok("Nuevo registro de usuario creado.");
        }
    }

    // Método PUT para actualizar el usuario especificado por su id, pudiéndose actualizar solo los campos
    // email, password y paymentCard.
    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUsuario(@RequestBody @Valid JSONObject camposUsuario,
                                                @PathVariable Integer id, BindingResult bindingResult,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws NoSuchAlgorithmException{
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {
            // Extraemos el token de la cabecera AUTHORIZATION.
            String token = extraerToken(authorizationHeader);

            Usuario usuarioPorActualizar = usuarioRepository.findById(id).orElseThrow(()
                    -> new UsuarioException("No se ha encontrado un usuario con ese id."));
            usuarioPorActualizar.setEmail(camposUsuario.getAsString("email"));
            
            // Se hashea la contraseña para que no aparezca como texto plano en la base de datos
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(camposUsuario.getAsString("password").getBytes());
            String passwordHash = Base64.getEncoder().encodeToString(hash);
            
            usuarioPorActualizar.setPassword(passwordHash);
            usuarioPorActualizar.setPaymentCard(camposUsuario.getAsString("paymentCard"));

            // Comprobamos si el usuario debería estar activado o no (se activa en el caso de que
            // tenga una tarjeta de pago y al menos un vehículo asociado.
            usuarioPorActualizar.setEnabled(!usuarioPorActualizar.getPaymentCard().isEmpty()
                    && !this.vehiculoService.getVehiclesByUserId(usuarioPorActualizar.getId(), token).isEmpty());

            try {
                usuarioRepository.save(usuarioPorActualizar);
            }catch (Exception e){
                throw new UsuarioException("Error al actualizar el usuario");
            }
            return ResponseEntity.ok("Usuario actualizado correctamente.");
        }
    }

    // Método DELETE para eliminar el usuario, especifidado por su id, así como los vehículos que pueda tener asociados.
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Integer id,
                                                @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){

        String token = extraerToken(authorizationHeader);

        // Buscamos si hay coincidencia entre el id que se nos ha pasado y alguno de los ids de los
        // usuarios almacenados en la BD.
        Usuario usuarioAEliminar = usuarioRepository.findById(id).orElseThrow(() ->
                new UsuarioException("No se ha encontrado un usuario con ese id."));

        // Eliminamos los vehículos asignados al Usuario, poniendo primero la referencia de cada uno a null
        // para no incumplir con ninguna restricción de clave foránea.
        List<VehiculoDTO> vehiculosAEliminar = vehiculoService.getVehiclesByUserId(usuarioAEliminar.getId(), token);
        for (VehiculoDTO vehiculo : vehiculosAEliminar) {
            vehiculo.userId = null;
            vehiculoService.putVehiculo(vehiculo, token);
            vehiculoService.deleteVehiculoPorId(vehiculo.id, token);
        }


        // Eliminamos al propio usuario y con ello los vehículos que pudieran estar a su nombre.
        usuarioRepository.delete(usuarioAEliminar);
        return ResponseEntity.ok("Registro de usuario borrado");
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
