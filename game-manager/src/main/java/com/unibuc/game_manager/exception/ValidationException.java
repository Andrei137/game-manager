package com.unibuc.game_manager.exception;

public final class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String template, Object... args) {
        super(String.format(template, args));
    }
}