package com.rok.controlelr;

import com.rok.exception.InvalidRequest;
import com.rok.exception.PostNotFound;
import com.rok.exception.RokLogException;
import com.rok.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    /*@ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();

        return response;
    }*/

    @ResponseBody
    @ExceptionHandler(RokLogException.class)
    public ResponseEntity rokLogException(RokLogException e) {
        int statusCode = e.statusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

/*
        if (e instanceof InvalidRequest) {
            InvalidRequest invalidRequest = (InvalidRequest) e;
            String filedName = invalidRequest.getFiledName();
            String message = invalidRequest.getMessage();
            body.addValidation(filedName, message);
        }
*/

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);


        return response;
    }


}
