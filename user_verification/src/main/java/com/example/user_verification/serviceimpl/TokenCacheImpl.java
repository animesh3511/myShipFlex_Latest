/*
package com.example.user_verification.serviceimpl;

import com.example.user_verification.service.TokenCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenCacheImpl implements TokenCache {

    private final Cache<Long, String> cache;

    public TokenCacheImpl() {
        cache = CacheBuilder.newBuilder().maximumSize(1000)
                .expireAfterAccess(60, TimeUnit.MINUTES).build();
    }

    @Override
    public void put(Long companyId, String token) {
        cache.put(companyId, token);
    }

    @Override
    public String get(Long companyId) {
        return cache.getIfPresent(companyId);
    }

   // @Override
   // public void invalidate(Long companyId) {
     //   cache.invalidate(companyId);
    //}
}
*/
