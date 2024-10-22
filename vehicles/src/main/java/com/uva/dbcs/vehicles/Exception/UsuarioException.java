package com.uva.dbcs.vehicles.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class UsuarioException extends RuntimeException{
    public UsuarioException(String mensaje ){super(mensaje);}
}
