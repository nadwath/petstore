package com.caceis.petstore.controller;

import com.caceis.petstore.dto.LoginRequestDTO;
import com.caceis.petstore.dto.RefreshTokenRequestDTO;
import com.caceis.petstore.service.AuthService;
import com.caceis.petstore.service.RsaKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    private final RsaKeyService rsa;

    public AuthController(AuthService auth, RsaKeyService rsa) {
        this.auth = auth;
        this.rsa = rsa;
    }

    @GetMapping("/public-key")
    public Map<String, String> pub() {
        var key = Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded());
        return Map.of("algorithm", "RSA", "key", key);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        PrivateKey priv = rsa.getPrivateKey();
        cipher.init(Cipher.DECRYPT_MODE, priv);
        byte[] plain = cipher.doFinal(Base64.getDecoder().decode(req.getEncryptedPassword()));
        var tokens = auth.login(req.getUsername(), new String(plain));

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequestDTO req) {
        return ResponseEntity.ok(auth.refresh(req.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        auth.logout(token);
        return ResponseEntity.ok().build();
    }
}
