package com.crm.ehelpdesk.config.security;

import com.crm.ehelpdesk.config.ApplicationProperties;
import com.crm.ehelpdesk.config.util.RandomUtil;
import com.crm.ehelpdesk.dao.repository.PersistentTokenRepository;
import com.crm.ehelpdesk.dao.repository.UserRepository;
import com.crm.ehelpdesk.domain.PersistentToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
public class PersistentTokenRememberMeServices extends
  AbstractRememberMeServices {

  private final Logger log = LoggerFactory.getLogger(PersistentTokenRememberMeServices.class);

  private static final int TOKEN_VALIDITY_DAYS = 31;

  private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;

  private static final long UPGRADED_TOKEN_VALIDITY_MILLIS = 5000l;

  private final PersistentTokenCache<UpgradedRememberMeToken> upgradedTokenCache;

  private final PersistentTokenRepository persistentTokenRepository;

  private final UserRepository userRepository;

  public PersistentTokenRememberMeServices(ApplicationProperties applicationProperties,
                                           UserDetailsService userDetailsService,
                                           PersistentTokenRepository persistentTokenRepository, UserRepository userRepository) {

    super(applicationProperties.getSecurity().getRememberMe().getKey(), userDetailsService);
    this.persistentTokenRepository = persistentTokenRepository;
    this.userRepository = userRepository;
    upgradedTokenCache = new PersistentTokenCache<>(UPGRADED_TOKEN_VALIDITY_MILLIS);
  }

  @Override
  protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
                                               HttpServletResponse response) {

    synchronized (this) {
      String login = null;
      UpgradedRememberMeToken upgradedToken = upgradedTokenCache.get(cookieTokens[0]);
      if (upgradedToken != null) {
        login = upgradedToken.getUserLoginIfValid(cookieTokens);
        log.debug("Detected previously upgraded login token for user '{}'", login);
      }

      if (login == null) {
        PersistentToken token = getPersistentToken(cookieTokens);
        login = token.getUser().getLogin();

        log.debug("Refreshing persistent login token for user '{}', series '{}'", login, token.getSeries());
        token.setTokenDate(LocalDate.now());
        token.setTokenValue(RandomUtil.generateTokenData());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try {
          persistentTokenRepository.saveAndFlush(token);
        } catch (DataAccessException e) {
          log.error("Failed to update token: ", e);
          throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
        }
        addCookie(token, request, response);
        upgradedTokenCache.put(cookieTokens[0], new UpgradedRememberMeToken(cookieTokens, login));
      }
      return getUserDetailsService().loadUserByUsername(login);
    }
  }

  @Override
  protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication
    successfulAuthentication) {

    String login = successfulAuthentication.getName();

    log.debug("Creating new persistent login for user {}", login);
    PersistentToken token = userRepository.findOneByLogin(login).map(u -> {
      PersistentToken t = new PersistentToken();
      t.setSeries(RandomUtil.generateSeriesData());
      t.setUser(u);
      t.setTokenValue(RandomUtil.generateTokenData());
      t.setTokenDate(LocalDate.now());
      t.setIpAddress(request.getRemoteAddr());
      t.setUserAgent(request.getHeader("User-Agent"));
      return t;
    }).orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));
    try {
      persistentTokenRepository.saveAndFlush(token);
      addCookie(token, request, response);
    } catch (DataAccessException e) {
      log.error("Failed to save persistent token ", e);
    }
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String rememberMeCookie = extractRememberMeCookie(request);
    if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
      try {
        String[] cookieTokens = decodeCookie(rememberMeCookie);
        PersistentToken token = getPersistentToken(cookieTokens);
        persistentTokenRepository.deleteById(token.getSeries());
      } catch (InvalidCookieException ice) {
        log.info("Invalid cookie, no persistent token could be deleted", ice);
      } catch (RememberMeAuthenticationException rmae) {
        log.debug("No persistent token found, so no token could be deleted", rmae);
      }
    }
    super.logout(request, response, authentication);
  }

  private PersistentToken getPersistentToken(String[] cookieTokens) {
    if (cookieTokens.length != 2) {
      throw new InvalidCookieException("Cookie token did not contain " + 2 +
        " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
    }
    String presentedSeries = cookieTokens[0];
    String presentedToken = cookieTokens[1];
    Optional<PersistentToken> optionalToken = persistentTokenRepository.findById(presentedSeries);
    if (!optionalToken.isPresent()) {
      throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
    }
    PersistentToken token = optionalToken.get();
    log.info("presentedToken={} / tokenValue={}", presentedToken, token.getTokenValue());
    if (!presentedToken.equals(token.getTokenValue())) {
      persistentTokenRepository.deleteById(token.getSeries());
      throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous " +
        "cookie theft attack.");
    }
    if (token.getTokenDate().plusDays(TOKEN_VALIDITY_DAYS).isBefore(LocalDate.now())) {
      persistentTokenRepository.deleteById(token.getSeries());
      throw new RememberMeAuthenticationException("Remember-me login has expired");
    }
    return token;
  }

  private void addCookie(PersistentToken token, HttpServletRequest request, HttpServletResponse response) {
    setCookie(
      new String[]{token.getSeries(), token.getTokenValue()},
      TOKEN_VALIDITY_SECONDS, request, response);
  }

  private static class UpgradedRememberMeToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String[] upgradedToken;

    private final String userLogin;

    UpgradedRememberMeToken(String[] upgradedToken, String userLogin) {
      this.upgradedToken = upgradedToken;
      this.userLogin = userLogin;
    }

    String getUserLoginIfValid(String[] currentToken) {
      if (currentToken[0].equals(this.upgradedToken[0]) &&
        currentToken[1].equals(this.upgradedToken[1])) {
        return this.userLogin;
      }
      return null;
    }
  }
}
