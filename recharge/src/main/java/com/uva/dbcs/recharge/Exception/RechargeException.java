package com.uva.dbcs.recharge.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class RechargeException extends RuntimeException{

    public RechargeException(String mensaje){ super(mensaje); }
}
