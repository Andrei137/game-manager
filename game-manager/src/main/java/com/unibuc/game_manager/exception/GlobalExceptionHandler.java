package com.unibuc.game_manager.exception;

import com.unibuc.game_manager.utils.ResponseUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseUtils.notFound(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorsList = new ArrayList<>();
        ex
                .getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorsList.add(error.getDefaultMessage()));
        Object responseBody;
        if (errorsList.size() == 1) {
            responseBody = errorsList.get(0);
        } else {
            responseBody = errorsList;
        }
        return ResponseUtils.badRequest(responseBody);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        switch (ex.getErrorCode()) {
            case 1062:
                final int index = message.indexOf("for key");
                message = ex.getMessage().substring(0, index).trim();
        }
        return ResponseUtils.badRequest(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormatEx) {
            List<String> errorsList = new ArrayList<>();
            invalidFormatEx.getPath().forEach(ref -> {
                errorsList.add("Invalid date value. Please use the correct format (yyyy-mm-dd)");
            });
            return ResponseUtils.badRequest(errorsList);
        }
        String message = ex.getMessage();
        if (message.contains("Required request body is missing")) {
            return ResponseUtils.badRequest("Request body is missing");
        }
        return ResponseUtils.badRequest(message);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        return ResponseUtils.badRequest(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
        return ResponseUtils.unauthorized(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException ex) {
        return ResponseUtils.forbidden(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return ResponseUtils.notFound(ex.getMessage());
    }


}