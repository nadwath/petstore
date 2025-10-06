package com.caceis.petstore.service.impl;

import com.caceis.petstore.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    private final Key key;
    private final long accessMinutes;
    private final long refreshDays;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret,
                          @Value("${jwt.accessMinutes}") long accessMinutes,
                          @Value("${jwt.refreshDays}") long refreshDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    public Pair issue(String username, Collection<String> rights) {
        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Instant accessExp = now.plusSeconds(accessMinutes * 60);
        Instant refreshExp = now.plusSeconds(refreshDays * 24 * 60 * 60);

        String access = Jwts.builder()
                .setId(jti)
                .setSubject(username)
                .claim("rights", rights)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(accessExp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refresh = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(username).claim("type", "refresh")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(refreshExp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new Pair(access, refresh, accessExp, refreshExp);
    }

    public Jws<Claims> parse(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
    }
}
