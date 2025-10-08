package com.caceis.petstore.exception;

public class PetSoldOutException extends RuntimeException {
    public PetSoldOutException(String message) {
        super(message);
    }
}
