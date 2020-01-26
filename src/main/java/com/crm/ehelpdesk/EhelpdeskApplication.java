package com.crm.ehelpdesk;

import com.crm.ehelpdesk.config.ApplicationProperties;
import com.crm.ehelpdesk.config.util.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
@EnableConfigurationProperties({
  ApplicationProperties.class,
})
@EnableAsync
public class EhelpdeskApplication {

  private final Environment environment;

  private static final Logger logger = LoggerFactory.getLogger(EhelpdeskApplication.class);

  public EhelpdeskApplication(Environment environment) {
    this.environment = environment;
  }

  @PostConstruct
  public void initApplication() {
    Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
    logger.info("Application Starting with Profile {}", activeProfiles);
  }

  public static void main(String[] args) throws UnknownHostException {
    System.setProperty("jasypt.encryptor.password", "password");
    SpringApplication app = new SpringApplication(EhelpdeskApplication.class);
    DefaultProfileUtil.addDefaultProfile(app);
    Environment env = app.run(args).getEnvironment();
    logger.info(
        "\n----------------------------------------------------------\n\t"
            + "Application '{}' is running! Access URLs:\n\t"
            + "Local: \t\thttp://localhost:{}\n\t"
            + "External: \thttp://{}:{}\n\t"
            + "Profile(s): \t{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        env.getProperty("server.port"),
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"),
        env.getActiveProfiles());
  }
}
