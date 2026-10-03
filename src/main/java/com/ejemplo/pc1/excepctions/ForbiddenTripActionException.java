package com.ejemplo.pc1.excepctions;

public class ForbiddenTripActionException extends RuntimeException {
    public ForbiddenTripActionException(String message) {
        super(message);
    }
}
