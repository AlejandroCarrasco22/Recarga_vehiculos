package com.uva.dbcs.chargerpoints.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.UNPROCESSABLE_ENTITY)
public class PuntoRecargaException extends RuntimeException{
    public PuntoRecargaException(String mensaje){super(mensaje);}
}
