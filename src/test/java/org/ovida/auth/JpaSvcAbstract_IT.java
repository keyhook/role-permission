package org.ovida.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import junitparams.JUnitParamsRunner;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.ovida.auth.app.account.AccountAppSvc;
import org.ovida.auth.app.role.RoleAppSvc;
import org.ovida.auth.helper.JpaTestHelper;
import org.ovida.auth.infra.jackson.JacksonMarker;
import org.ovida.auth.infra.jpa.JpaMarker;
import org.ovida.auth.infra.springtxn.SpringTxnMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(JUnitParamsRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = "classpath:CleanUp.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ContextConfiguration
public abstract class JpaSvcAbstract_IT {

  @TestConfiguration
  @ComponentScan(basePackageClasses = {
    JpaMarker.class,
    JacksonMarker.class,
    SpringTxnMarker.class,
    AccountAppSvc.class,
    RoleAppSvc.class,
    JpaTestHelper.class
  })
  public static class TestConfig {

  }

  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();

  @Autowired
  protected JpaTestHelper jpaTestHelper;

  protected AbstractStringAssert<?> assertThatMsgOfDataIntegrityViolationEx(ThrowingCallable throwingCallable) {
    return assertThatThrownBy(throwingCallable)
      .isInstanceOf(DataIntegrityViolationException.class)
      .extracting(Throwable::getCause)
      .extracting(Throwable::getCause)
      .extracting(Throwable::getMessage)
      .asString();
  }
}
