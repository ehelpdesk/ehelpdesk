package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.LoginRepository;
import com.crm.ehelpdesk.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {
  private final LoginRepository loginRepository;
  private final CacheService cacheService;
  private final MailService mailService;

  public LoginService(LoginRepository loginRepository, CacheService cacheService, MailService mailService) {
    this.loginRepository = loginRepository;
    this.cacheService = cacheService;
    this.mailService = mailService;
  }

  public void removeUserOtp(String username) {
    cacheService.removeUserOtp(username);
  }
}
