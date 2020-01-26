package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.LoginRepository;
import com.crm.ehelpdesk.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

  public boolean hasActiveLogin(String loginName) {
    return loginRepository.findOneByLogin(loginName).map(User::isActiveLogin).orElse(false);
  }

  public void updateActiveLogin(String username, boolean activeLogin) {
    cacheService.addActiveLogin(username);
    loginRepository.findOneByLogin(username).ifPresent(user -> {
      user.setActiveLogin(activeLogin);
      loginRepository.save(user);
    });
  }

  public void removeUserOtp(String username) {
    cacheService.removeUserOtp(username);
  }

  public boolean isValidCredentials(Map<String, Object> credentials) {
    boolean isValidCredentials = false;
    String username = (String) credentials.get("username");
    String password = (String) credentials.get("password");
    if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
      Optional<User> loginOptional = loginRepository.findOneByLogin(username);
      if (loginOptional.isPresent()) {
        User user = loginOptional.get();
        String userPassword = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        isValidCredentials = bCryptPasswordEncoder.matches(password, userPassword);
        if(isValidCredentials) {
          String otp = RandomStringUtils.randomNumeric(5);
          user.setOtp(otp);
          cacheService.addUserOtp(user.getLogin(), otp);
          mailService.sendOtpEmail(user);
        }
      }
    }
    return isValidCredentials;
  }
}
