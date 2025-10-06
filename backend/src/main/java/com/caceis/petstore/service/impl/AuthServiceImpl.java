package com.caceis.petstore.service.impl;

import com.caceis.petstore.service.TokenAllowListService;
import com.caceis.petstore.domain.User;
import com.caceis.petstore.repo.UserRepo;
import com.caceis.petstore.service.JwtService;
import com.caceis.petstore.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo users;
    private final JwtService jwt;
    private final TokenAllowListService allow;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Tokens login(String username, String rawPassword) {
        User u = users.findByUsername(username).orElseThrow();

        if (!encoder.matches(rawPassword, u.getPassword()))
            throw new BadCredentialsException("Username or password is incorrect!");

        var pair = jwt.issue(u.getUsername(), u.getRights());
        Claims c = jwt.parse(pair.accessToken()).getBody();
        allow.allow(c.getId(), Duration.between(c.getIssuedAt().toInstant(), c.getExpiration().toInstant()));

        return new Tokens(pair.accessToken(), pair.refreshToken());
    }

    @Override
    public Tokens refresh(String refreshToken) {
        var jws = jwt.parse(refreshToken);
        var c = jws.getBody();

        if (!"refresh".equals(c.get("type")))
            throw new RuntimeException("invalid_refresh");

        String username = c.getSubject();
        var u = users.findByUsername(username).orElseThrow();
        var pair = jwt.issue(u.getUsername(), u.getRights());
        Claims ac = jwt.parse(pair.accessToken()).getBody();
        allow.allow(ac.getId(), Duration.between(ac.getIssuedAt().toInstant(), ac.getExpiration().toInstant()));

        return new Tokens(pair.accessToken(), pair.refreshToken());
    }
}
