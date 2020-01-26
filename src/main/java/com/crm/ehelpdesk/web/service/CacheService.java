package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.ApplicationProperties;
import com.crm.ehelpdesk.config.constants.CacheConstants;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {
  private final CacheManager cacheManager;
  private final ApplicationProperties applicationProperties;

  public CacheService(CacheManager cacheManager, ApplicationProperties applicationProperties) {
    this.cacheManager = cacheManager;
    this.applicationProperties = applicationProperties;
  }

  @SuppressWarnings("unchecked")
  void addActiveLogin(String login) {
    Cache cache = cacheManager.getCache(CacheConstants.ACTIVE_USER_EXPIRY_CACHE);
    if (cache != null) {
      Map<String, Instant> activeLogin;
      Cache.ValueWrapper activeLoginValueWrapper = cache.get(login);
      if (activeLoginValueWrapper == null) {
        activeLogin = new HashMap<>();
      } else {
        activeLogin = (Map<String, Instant>) activeLoginValueWrapper.get();
      }
      activeLogin.put(login,
        Instant.now().plus(
          Duration.ofSeconds(applicationProperties.getCache().getEhcache().getTimeToLiveSeconds())));
      cache.put(login, activeLogin);
    }
  }

  public void removeActiveLogin(String login) {
    Cache cache = cacheManager.getCache(CacheConstants.ACTIVE_USER_EXPIRY_CACHE);
    if (cache != null) {
      cache.evictIfPresent(login);
    }
  }

  void addUserOtp(String login, String otp) {
    Cache userOtpCache = cacheManager.getCache(CacheConstants.USER_OTP_CACHE);
    if (userOtpCache != null) {
      userOtpCache.put(login, otp);
    }
  }

  public String getUserOtp(String login) {
    String otp = "";
    Cache userOtpCache = cacheManager.getCache(CacheConstants.USER_OTP_CACHE);
    if (userOtpCache != null) {
      Cache.ValueWrapper otpValueWrapper = userOtpCache.get(login);
      if(otpValueWrapper != null) {
        otp = (String) otpValueWrapper.get();
      }
    }
    return otp;
  }

  public void removeUserOtp(String login) {
    Cache cache = cacheManager.getCache(CacheConstants.USER_OTP_CACHE);
    if (cache != null) {
      cache.evictIfPresent(login);
    }
  }

}
