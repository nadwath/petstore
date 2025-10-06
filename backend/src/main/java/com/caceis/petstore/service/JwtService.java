package com.caceis.petstore.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.time.Instant;
import java.util.Collection;

public interface JwtService {
    record Pair(String accessToken, String refreshToken, Instant accessExp, Instant refreshExp) {
    }

    Pair issue(String username, Collection<String> rights);

    Jws<Claims> parse(String jwt);
}
