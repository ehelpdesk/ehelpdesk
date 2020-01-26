package com.crm.ehelpdesk.config;

public interface EhelpdeskDefaults {

  interface Async {

    int corePoolSize = 2;
    int maxPoolSize = 50;
    int queueCapacity = 10000;
  }

  interface Http {

    interface Cache {

      int timeToLiveInDays = 1461;
    }
  }

  interface Cache {

    interface Ehcache {

      int timeToLiveSeconds = 10800;
      long maxEntries = 100;
    }

  }

  interface Mail {
    boolean enabled = false;
    String from = "alpha9ineinc@gmail.com";
    String baseUrl = "http://localhost:9006/";
  }

  interface Security {

    interface ClientAuthorization {

      String accessTokenUri = null;
      String tokenServiceId = null;
      String clientId = null;
      String clientSecret = null;
    }

    interface Authentication {

      interface Jwt {

        String secret = null;
        String base64Secret = null;
        long tokenValidityInSeconds = 1800; // 30 minutes
        long tokenValidityInSecondsForRememberMe = 2592000; // 30 days
      }
    }

    interface RememberMe {

      String key = null;
    }
  }

  interface Metrics {

    interface Jmx {

      boolean enabled = false;
    }

    interface Logs {

      boolean enabled = false;
      long reportFrequency = 60;

    }
  }

  interface Logging {

    boolean useJsonFormat = false;

    interface Logstash {

      boolean enabled = false;
      String host = "localhost";
      int port = 5000;
      int queueSize = 512;
    }
  }

  interface Social {

    String redirectAfterSignIn = "/#/home";
  }

  interface ClientApp {

    String name = "Ehelpdesk";
  }

  interface AuditEvents {

    int retentionPeriod = 30;
  }
}
