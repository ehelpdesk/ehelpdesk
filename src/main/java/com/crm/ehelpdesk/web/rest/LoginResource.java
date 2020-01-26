package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.web.service.LoginService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginResource {
  private final LoginService loginService;

  public LoginResource(LoginService loginService) {
    this.loginService = loginService;
  }

  @GetMapping("/hasActiveLogin/{loginName}")
  public boolean hasActiveLogin(@PathVariable String loginName) {
    return loginService.hasActiveLogin(loginName);
  }

  @PostMapping("/verifyCredentials")
  public boolean isValidCredentials(@RequestBody Map<String, Object> credentials) {
    return loginService.isValidCredentials(credentials);
  }

}
