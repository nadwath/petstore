package com.caceis.petstore.service;

public interface AuthService {
    Tokens login(String username, String rawPassword);
    Tokens refresh(String refreshToken);
    void logout(String accessToken);

    record Tokens(String accessToken, String refreshToken) {
    }
}
