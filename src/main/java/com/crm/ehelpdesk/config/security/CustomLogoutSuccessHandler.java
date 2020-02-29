package com.crm.ehelpdesk.config.security;

import com.crm.ehelpdesk.web.service.CacheService;
import com.crm.ehelpdesk.web.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  private final LoginService loginService;
  private final CacheService cacheService;

  public CustomLogoutSuccessHandler(LoginService loginService, CacheService cacheService) {
    this.loginService = loginService;
    this.cacheService = cacheService;
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) {
    String loginName = authentication.getName();
    if (loginName != null) {
      // cacheService.removeActiveLogin(loginName);
    }
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
