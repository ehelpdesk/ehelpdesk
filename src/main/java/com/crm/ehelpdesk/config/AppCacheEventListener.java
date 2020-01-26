package com.crm.ehelpdesk.config;/*
package com.crm.ehelpdesk.config;

import com.crm.ehelpdesk.web.service.LoginService;
import org.ehcache.event.CacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppCacheEventListener implements org.ehcache.event.CacheEventListener<Object, Object>, ApplicationContextAware {
  private final Logger log = LoggerFactory.getLogger(AppCacheEventListener.class);
  private LoginService loginService;

  @Override
  public void onEvent(CacheEvent<? extends Object, ? extends Object> event) {
    if (event.getKey() != null) {
      loginService.updateActiveLogin((String) event.getKey(), false);
      log.info("Cache Evicted: " + event.getKey() + ": " + event.getNewValue());
    }
  }

  @Override
  public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
    this.loginService = applicationContext.getBean(LoginService.class);
  }
}
*/
