package com.caceis.petstore.service;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface RsaKeyService {
    PublicKey getPublicKey();
    PrivateKey getPrivateKey();
}
