package com.uva.dbcs.login.Controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import com.uva.dbcs.login.Exception.AuthException;
import com.uva.dbcs.sharedlibrary.dto.UsuarioDTO;
import com.uva.dbcs.sharedlibrary.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/paginaPrincipal")
@CrossOrigin(origins="*")
public class AuthController {
    private final UsuarioService usuarioService;

    AuthController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    // Metodo que permite comprobar si un usuario está registrado en la base de datos y en caso de que se correcto
    // se crea un token JWT para que Angular más tarde lo gestione.

    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioDTO usuarioLogeado, BindingResult bindingResult) throws NoSuchAlgorithmException{
        if (bindingResult.hasErrors()) {
            // Procesa los errores de validación y devuelve una respuesta apropiada
            String errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Errores de validación: " + errors);
        } else {

            // El usuario encontrado en la BD de Usuarios por medio de los datos introducidos en el login.
            UsuarioDTO usuarioEncontrado = null;

            // Se comprueba si existe algún usuario con ese email.
            if((usuarioEncontrado = usuarioService.getUserByEmail(usuarioLogeado.email)) == null){
                throw new AuthException("No hay registros de usuario con ese email.");
            }

            // Se comprueba si la contraseña introducida coincide con la contraseña del usuario cuyo
            // email se ha introducido. En caso de no haber coincidencia, se trata de un error. Antes
            // de hacer la comprobación, hasheamos la contraseña introducida en el login, pues
            // todas las contraseñas guardadas en la BD se guardan como un hash y no en texto plano.
            // Si la contraseña introducida es la del usuario cuyo email se ha introducido, los hash de
            // ambas contraseñas serán iguales.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(usuarioLogeado.password.getBytes());
            String passwordHash = Base64.getEncoder().encodeToString(hash);

            if(!Objects.equals(usuarioEncontrado.password, passwordHash)){
                throw new AuthException("Email y/o contraseña incorrectos");
            }

            // Generamos el token para el usuario autentificado.
            String token = generateToken(usuarioEncontrado.name);
            return ResponseEntity.ok(token);
        }
    }

    private static String generateToken(String username){
        String secret = "miClaveSecreta";

        // El keyId generado en Konga a partir del secret "miClaveSecreta"
        String keyId = "F18ebdet2scfZKlBriISG8fVO0QLJPZT";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");

        // Por ahora el token solo contiene el nombre de usuario. Faltaría el email.        

        String token = Jwts
				.builder()
				.setId("echoJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
                .setHeaderParam("kid", keyId)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS256,
						secret.getBytes()).compact();

        return "Bearer " + token;
    }


}
