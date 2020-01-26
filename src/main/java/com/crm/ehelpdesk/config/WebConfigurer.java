package com.crm.ehelpdesk.config;

import com.crm.ehelpdesk.config.constants.ApplicationConstants;
import com.crm.ehelpdesk.config.filter.CachingHttpHeadersFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.EnumSet;

import static java.net.URLDecoder.decode;

@Configuration
public class WebConfigurer
  implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {

  private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

  private final Environment env;
  private final ApplicationProperties applicationProperties;

  public WebConfigurer(Environment env, ApplicationProperties applicationProperties) {

    this.env = env;
    this.applicationProperties = applicationProperties;
  }

  @Override
  public void onStartup(ServletContext servletContext) {
    if (env.getActiveProfiles().length != 0) {
      log.info(
        "Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
    }
    EnumSet<DispatcherType> disps =
      EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
    if (env.acceptsProfiles(Profiles.of(ApplicationConstants.SPRING_PROFILE_PRODUCTION))) {
      initCachingHttpHeadersFilter(servletContext, disps);
    }
    log.info("Web application fully configured");
  }

  @Override
  public void customize(WebServerFactory server) {
    setMimeMappings(server);
    setLocationForStaticAssets(server);
  }

  private void setMimeMappings(WebServerFactory server) {
    if (server instanceof ConfigurableServletWebServerFactory) {
      MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
      mappings.add(
        "html",
        MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
      mappings.add(
        "json",
        MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
      ConfigurableServletWebServerFactory servletWebServer =
        (ConfigurableServletWebServerFactory) server;
      servletWebServer.setMimeMappings(mappings);
    }
  }

  private void setLocationForStaticAssets(WebServerFactory server) {
    if (server instanceof ConfigurableServletWebServerFactory) {
      ConfigurableServletWebServerFactory servletWebServer =
        (ConfigurableServletWebServerFactory) server;
      File root;
      String prefixPath = resolvePathPrefix();
      root = new File(prefixPath + "build/www/");
      if (root.exists() && root.isDirectory()) {
        servletWebServer.setDocumentRoot(root);
      }
    }
  }

  private String resolvePathPrefix() {
    String fullExecutablePath;
    try {
      fullExecutablePath =
        decode(this.getClass().getResource("").getPath(), StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      fullExecutablePath = this.getClass().getResource("").getPath();
    }
    String rootPath = Paths.get(".").toUri().normalize().getPath();
    String extractedPath = fullExecutablePath.replace(rootPath, "");
    int extractionEndIndex = extractedPath.indexOf("build/");
    if (extractionEndIndex <= 0) {
      return "";
    }
    return extractedPath.substring(0, extractionEndIndex);
  }

  private void initCachingHttpHeadersFilter(
          ServletContext servletContext, EnumSet<DispatcherType> disps) {
    log.debug("Registering Caching HTTP Headers Filter");
    FilterRegistration.Dynamic cachingHttpHeadersFilter =
      servletContext.addFilter(
        "cachingHttpHeadersFilter", new CachingHttpHeadersFilter(applicationProperties));
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*");
    cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*");
    cachingHttpHeadersFilter.setAsyncSupported(true);
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = applicationProperties.getCors();
    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      log.debug("Registering CORS filter");
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/management/**", config);
    }
    return new CorsFilter(source);
  }
}
