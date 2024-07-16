package com.rok.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends RokLogException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    private String filedName;
    private String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String filedName, String message) {
        super(MESSAGE);
        addValidation(filedName, message);
    }

    public int statusCode() {
        return 400;
    }

}
