package com.crm.ehelpdesk.config.security;

import com.crm.ehelpdesk.web.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final LoginService loginService;

  public CustomAuthenticationSuccessHandler(LoginService loginService) {
    this.loginService = loginService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication)
    throws IOException, ServletException {
    loginService.removeUserOtp(authentication.getName());
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
