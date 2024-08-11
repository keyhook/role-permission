package org.ovida.auth;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import java.time.ZoneOffset;
import java.util.TimeZone;
import org.flywaydb.core.Flyway;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses("**/*IT.class")
public class JpaSuiteTest {

  static {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    System.setProperty(
      "log4j2.contextSelector",
      "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    System.setProperty(
      "java.util.logging.manager",
      "org.apache.logging.log4j.jul.LogManager");
  }

  private static PostgreSQLContainer<?> postgreSQLContainer;

  private static void startPostgreSqlContainer() {
    postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.3")
      .withStartupTimeoutSeconds(600);
    postgreSQLContainer.start();
  }

  private static void importSchemaForPostgreSqlContainer() {
    Flyway
      .configure()
      .dataSource(
        postgreSQLContainer.getJdbcUrl(),
        postgreSQLContainer.getUsername(),
        postgreSQLContainer.getPassword())
      .locations("classpath:db/migration")
      .load()
      .migrate();
  }

  private static void setApplicationProperties() {
    System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
    System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
  }

  @BeforeClass
  public static void before() {
    startPostgreSqlContainer();
    importSchemaForPostgreSqlContainer();
    setApplicationProperties();
  }

  @AfterClass
  public static void after() {
    postgreSQLContainer.stop();
  }
}
