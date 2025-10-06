package com.caceis.petstore.service.impl;

import com.caceis.petstore.service.TokenAllowListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenAllowListServiceImpl implements TokenAllowListService {
    private final StringRedisTemplate redis;

    @Override
    public void allow(String jti, Duration ttl) {
        redis.opsForValue().set("jwt:allow:" + jti, "1", ttl);
    }

    @Override
    public boolean isAllowed(String jti) {
        return "1".equals(redis.opsForValue().get("jwt:allow:" + jti));
    }

    @Override
    public void revoke(String jti) {
        redis.delete("jwt:allow:" + jti);
    }
}
