package com.caceis.petstore.controller;

import com.caceis.petstore.dto.LoginRequestDTO;
import com.caceis.petstore.dto.RefreshTokenRequestDTO;
import com.caceis.petstore.service.AuthService;
import com.caceis.petstore.service.RsaKeyService;
import org.springframework.http.HttpStatus;
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
    public AuthService.Tokens login(@RequestBody LoginRequestDTO req) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        PrivateKey priv = rsa.getPrivateKey();
        cipher.init(Cipher.DECRYPT_MODE, priv);
        byte[] plain = cipher.doFinal(Base64.getDecoder().decode(req.getEncryptedPassword()));
        return auth.login(req.getUsername(), new String(plain));
    }

    @PostMapping("/refresh")
    public AuthService.Tokens refresh(@RequestBody RefreshTokenRequestDTO req) {
        return auth.refresh(req.getRefreshToken());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        auth.logout(token);
    }
}
