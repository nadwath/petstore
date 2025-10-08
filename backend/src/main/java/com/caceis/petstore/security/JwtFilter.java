package com.caceis.petstore.security;

import com.caceis.petstore.service.JwtService;
import com.caceis.petstore.service.TokenAllowListService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final TokenAllowListService allow;

    public JwtFilter(JwtService jwt, TokenAllowListService allow) {
        this.jwt = jwt;
        this.allow = allow;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, IOException {
        String h = req.getHeader("Authorization");

        if (h != null && h.startsWith("Bearer ")) {
            String token = h.substring(7);

            try {
                var jws = jwt.parse(token);
                Claims c = jws.getBody();
                String jti = c.getId();

                if (allow.isAllowed(jti)) {
                    String user = c.getSubject();
                    var authorities = (List<String>) c.get("authorities");
                    List<SimpleGrantedAuthority> grantedAuthorities = authorities == null ? List.of() : authorities.stream().map(SimpleGrantedAuthority::new).toList();
                    var auth = new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ex) {
                log.error("Invalid JWT token: {}", ex.getMessage(), ex);
                SecurityContextHolder.clearContext();
            }
        }
        fc.doFilter(req, res);
    }
}
