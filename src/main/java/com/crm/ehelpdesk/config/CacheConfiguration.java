package com.crm.ehelpdesk.config;

import com.crm.ehelpdesk.config.constants.CacheConstants;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

  private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;
  // private final javax.cache.configuration.Configuration<Object, Object> jcacheWithEventConfiguration;

  public CacheConfiguration(ApplicationProperties applicationProperties) {
    ApplicationProperties.Cache.Ehcache ehcache = applicationProperties.getCache().getEhcache();

    /*CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
      .newEventListenerConfiguration(new AppCacheEventListener(), EventType.CREATED, EventType.EXPIRED)
      .unordered().asynchronous();*/

    CacheConfigurationBuilder<Object, Object> cacheConfigBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
      ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
      .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())));
    jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
      cacheConfigBuilder
        .build());

    /*jcacheWithEventConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
      cacheConfigBuilder
        .withService(cacheEventListenerConfiguration)
        .build());*/
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      createCache(cm, CacheConstants.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
      createCache(cm, CacheConstants.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
      createCache(cm, CacheConstants.USER_OTP_CACHE, jcacheConfiguration);
      // createCache(cm, CacheConstants.ACTIVE_USER_EXPIRY_CACHE, jcacheConfiguration);
      //createCache(cm, CacheConstants.ACTIVE_USER_EXPIRY_CACHE, jcacheWithEventConfiguration);
    };
  }

  private void createCache(CacheManager cm, String cacheName, javax.cache.configuration.Configuration<Object, Object> cacheConfig) {
    javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
    if (cache != null) {
      cm.destroyCache(cacheName);
    }
    cm.createCache(cacheName, cacheConfig);
  }

}
