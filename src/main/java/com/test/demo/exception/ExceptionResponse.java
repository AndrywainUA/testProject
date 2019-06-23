package com.test.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionResponse {

    private static ResponseEntity notFoundErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    private static ResponseEntity badRequestErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    private static ResponseEntity internalErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    private static ResponseEntity forbiddenErrorResponse(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    public static ResponseEntity badRequestError(String msg){
        return badRequestErrorResponse(msg);
    }
    public static ResponseEntity notFoundError(String msg){
        return notFoundErrorResponse(msg);
    }
    public static ResponseEntity forbiddenError(String msg){
        return forbiddenErrorResponse(msg);
    }
    public static ResponseEntity internalError(String msg){
        return internalErrorResponse(msg);
    }

}
