package com.crm.ehelpdesk.security;

import com.crm.ehelpdesk.config.security.AuthoritiesConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                }
                return null;
            });
    }

    public static boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

  private static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static List<String> getUserAuthorities() {
    Authentication authentication = getAuthentication();
    return getAuthorities(authentication).collect(Collectors.toList());
  }

  public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = getAuthentication();
        return authentication != null &&
            getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority);
    }

}
