package com.example.FootballLeagues.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException{

    private String message;

    public ObjectNotFoundException(String message) {
        super("Object with this id/name:" + message + "wasn't found!");
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
