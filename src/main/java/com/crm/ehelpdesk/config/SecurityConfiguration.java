package com.crm.ehelpdesk.config;


import com.crm.ehelpdesk.config.security.AuthoritiesConstants;
import com.crm.ehelpdesk.config.security.CustomAuthenticationFailureHandler;
import com.crm.ehelpdesk.config.security.CustomAuthenticationSuccessHandler;
import com.crm.ehelpdesk.config.security.CustomLogoutSuccessHandler;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final RememberMeServices rememberMeServices;

  private final CorsFilter corsFilter;
  private final SecurityProblemSupport problemSupport;
  private final ApplicationProperties applicationProperties;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  public SecurityConfiguration(RememberMeServices rememberMeServices, CorsFilter corsFilter,
                               SecurityProblemSupport problemSupport, ApplicationProperties applicationProperties,
                               CustomLogoutSuccessHandler customLogoutSuccessHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
    this.rememberMeServices = rememberMeServices;
    this.corsFilter = corsFilter;
    this.problemSupport = problemSupport;
    this.applicationProperties = applicationProperties;
    this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
  }


  @Bean
  public CustomAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
      .antMatchers(HttpMethod.OPTIONS, "/**")
      .antMatchers("/app/**/*.{js,html}")
      .antMatchers("/i18n/**")
      .antMatchers("/content/**")
      .antMatchers("/test/**");
  }


  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public static ServletListenerRegistrationBean httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
      .csrf()
      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      .and()
      .addFilterBefore(corsFilter, CsrfFilter.class)
      .exceptionHandling()
      .authenticationEntryPoint(problemSupport)
      .accessDeniedHandler(problemSupport)
      .and()
      .rememberMe()
      .rememberMeServices(rememberMeServices)
      .rememberMeParameter("remember-me")
      .key(applicationProperties.getSecurity().getRememberMe().getKey())
      .and()
      .formLogin()
      .loginProcessingUrl("/api/authentication")
      .successHandler(customAuthenticationSuccessHandler)
      .failureHandler(ajaxAuthenticationFailureHandler())
      .permitAll()
      .and()
      .logout()
      .logoutUrl("/api/logout")
      .logoutSuccessHandler(customLogoutSuccessHandler)
      .permitAll()
      .and()
      .headers()
      .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
      .and()
      .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
      .and()
      .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
      .and()
      .frameOptions()
      .deny()
      .and()
      .authorizeRequests()
      .antMatchers("/api/authentication").permitAll()
      .antMatchers("/api/authenticate").permitAll()
      .antMatchers("/api/main/password").permitAll()
      .antMatchers("/api/register").permitAll()
      .antMatchers("/api/activate").permitAll()
      .antMatchers("/api/verifyCredentials").permitAll()
      .antMatchers("/api/hasActiveLogin/**").permitAll()
      .antMatchers("/api//account/forgot-username").permitAll()
      .antMatchers("/api/account/reset-password/start").permitAll()
      .antMatchers("/api/account/reset-password/finish").permitAll()
      .antMatchers("/api/**").authenticated()
      .antMatchers("/management/health").permitAll()
      .antMatchers("/management/info").permitAll()
      .antMatchers("/management/prometheus").permitAll()
      .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);

    http
      .sessionManagement()
      .maximumSessions(1).
      sessionRegistry(sessionRegistry());
    // @formatter:on
  }
}
