package org.ovida.auth;

import java.time.ZoneOffset;
import java.util.Objects;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    System.setProperty(
      "log4j2.contextSelector",
      "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    System.setProperty(
      "java.util.logging.manager",
      "org.apache.logging.log4j.jul.LogManager");
    if (Objects.isNull(System.getProperty("spring.profiles.active"))) {
      System.setProperty("spring.profiles.active", "local");
    }
    SpringApplication.run(Application.class, args);
  }

}
