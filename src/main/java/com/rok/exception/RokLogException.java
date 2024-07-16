package com.rok.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class RokLogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public RokLogException(String message) {
        super(message);
    }

    public RokLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();

    public void addValidation(String filedName, String message) {
        validation.put(filedName, message);
    }

}
