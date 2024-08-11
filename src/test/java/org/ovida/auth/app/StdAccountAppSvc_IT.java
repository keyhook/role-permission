package org.ovida.auth.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;
import junitparams.Parameters;
import org.junit.Test;
import org.ovida.auth.JpaSvcAbstract_IT;
import org.ovida.auth.app.account.StdAccountAppSvc;
import org.ovida.auth.app.search.SearchQuery;
import org.ovida.auth.domain.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

public class StdAccountAppSvc_IT extends JpaSvcAbstract_IT {

  @Autowired
  private StdAccountAppSvc stdAccountAppSvc;

  private Object[] parametersForSearchReturnsCorrectResults() {
    return new Object[]{
      new Object[]{0, 10, 12, 10, List.of(
        UUID.fromString("00000000-0000-0000-0000-000000000012"),
        UUID.fromString("00000000-0000-0000-0000-000000000011"),
        UUID.fromString("00000000-0000-0000-0000-000000000010"),
        UUID.fromString("00000000-0000-0000-0000-000000000009"),
        UUID.fromString("00000000-0000-0000-0000-000000000008"),
        UUID.fromString("00000000-0000-0000-0000-000000000007"),
        UUID.fromString("00000000-0000-0000-0000-000000000006"),
        UUID.fromString("00000000-0000-0000-0000-000000000005"),
        UUID.fromString("00000000-0000-0000-0000-000000000004"),
        UUID.fromString("00000000-0000-0000-0000-000000000003"))},
      new Object[]{1, 10, 12, 2, List.of(
        UUID.fromString("00000000-0000-0000-0000-000000000002"),
        UUID.fromString("00000000-0000-0000-0000-000000000001"))},
      new Object[]{2, 10, 12, 0, List.of()},
    };
  }

  @Test
  @Sql("StdAccountAppSvc_Case.sql")
  @Parameters(method = "parametersForSearchReturnsCorrectResults")
  public void shouldReturnCorrectResults_WhenSearch(int page, int pageSize, int total, int recordsSize, List<UUID> accountIds) {
    var searchQuery = new SearchQuery().setPage(page).setPageSize(pageSize);

    var result = stdAccountAppSvc.search(searchQuery);

    assertEquals(total, result.getTotal());
    assertEquals(recordsSize, result.getRecords().size());
    assertEquals(page, result.getPage());
    assertEquals(pageSize, result.getPageSize());

    assertThat(result.getRecords())
      .extracting("id")
      .containsAll(accountIds);
  }

  @Test
  @Sql("StdAccountAppSvc_Case.sql")
  @Parameters({
    "00000000-0000-0000-0000-000000000001, admin@admin.com",
    "00000000-0000-0000-0000-000000000006, member2@example.com",
    "00000000-0000-0000-0000-000000000012, member5@example.com"
  })
  public void shouldReturnCorrectAccount_WhenGet(String accountId, String email) {
    var account = stdAccountAppSvc.get(UUID.fromString(accountId));

    assertEquals(account.getEmail(), email);
  }

  @Test
  public void shouldThrowNotFoundException_WhenGetNoAccount() {
    var notFoundId = UUID.randomUUID();

    assertThatThrownBy(() -> stdAccountAppSvc.get(notFoundId))
      .isInstanceOf(DomainException.class)
      .hasMessageContainingAll("ENTITY_NOT_FOUND", notFoundId.toString());
  }
}
