package com.crm.ehelpdesk.security;

import com.crm.ehelpdesk.dao.repository.UserRepository;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.exception.UserNotActivatedException;
import com.crm.ehelpdesk.web.service.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

  private final UserRepository userRepository;
  private final CacheService cacheService;

  public DomainUserDetailsService(UserRepository userRepository, CacheService cacheService) {
    this.userRepository = userRepository;
    this.cacheService = cacheService;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String loginElement) {
    String[] loginElements = loginElement.split("\\|\\|\\|");
    final String login = loginElements[0];
    final String otp = loginElements[1];
    String userOtp = cacheService.getUserOtp(login);
    log.debug("Authenticating {}", login);
    if (!StringUtils.equals(otp, userOtp)) {
      throw new UsernameNotFoundException("OTP for the user " + login + " is invalid");
    }
    if (new EmailValidator().isValid(login, null)) {
      return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
        .map(user -> createSpringSecurityUser(login, user))
        .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
    }

    String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
    return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
      .map(user -> createSpringSecurityUser(lowercaseLogin, user))
      .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));

  }

  private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
    if (!user.getActivated()) {
      throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
    }
    List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>() {{
      add(new SimpleGrantedAuthority(user.getAuthorities().getName()));
    }};
    return new org.springframework.security.core.userdetails.User(user.getLogin(),
      user.getPassword(), grantedAuthorities);
  }
}
