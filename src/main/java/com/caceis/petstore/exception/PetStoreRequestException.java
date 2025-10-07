package com.caceis.petstore.exception;

public class PetStoreRequestException extends RuntimeException {
    public PetStoreRequestException(String message) {
        super(message);
    }

    public PetStoreRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
