package com.example.user_verification.serviceimpl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OtpService {

    private static final Integer EXPIRE_MINS = 5;
    //private LoadingCache<String, Integer> cache;
    private LoadingCache<String, Integer> cache;

    public OtpService() {
        super();
        cache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String s) throws Exception {
                return 0;
            }
        });
    }

    public int generateOTP(String key) {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);
        System.out.println(" generated otp:-" + otp);
        cache.put(key, otp);

        return otp;
    }

    public int getOtp(String key) {
        try {
            log.debug("Get OTP By Email " + key);
            System.out.println("Otp stored in cache "+cache.get(key));
            return cache.get(key);
        } catch (Exception e) {
            log.error("No Otp Found For The Email = " + key);
            return 0;
        }
    }

    public void clearOTP(String key) {
        cache.invalidate(key);
    }

//class ends here
}
