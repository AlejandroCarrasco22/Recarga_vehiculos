package com.uva.dbcs.login.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class AuthException extends RuntimeException{


    public AuthException(String mensaje ){super(mensaje);}

}