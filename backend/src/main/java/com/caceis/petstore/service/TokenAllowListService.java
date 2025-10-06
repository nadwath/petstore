package com.caceis.petstore.service;

import java.time.Duration;

public interface TokenAllowListService {
    void allow(String jti, Duration ttl);

    boolean isAllowed(String jti);

    void revoke(String jti);
}
