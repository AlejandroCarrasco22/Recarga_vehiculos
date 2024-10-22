package com.uva.dbcs.vehicles.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class VehiculoException extends RuntimeException{
    public VehiculoException(String mensaje){super(mensaje);}
}
