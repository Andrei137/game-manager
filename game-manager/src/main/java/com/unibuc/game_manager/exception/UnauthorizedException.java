package com.unibuc.game_manager.exception;

public final class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}