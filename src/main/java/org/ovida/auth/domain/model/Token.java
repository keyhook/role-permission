package org.ovida.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class Token {

  private UUID id;

  private UUID accountId;

  private String email;

  private String roleName;

  private Set<String> privilegeNames;

  private LocalDateTime expiredAt;

  public boolean hasAccountId(UUID accountId) {
    return this.accountId.equals(accountId);
  }

  public boolean hasReadAccountPrivilege() {
    return privilegeNames.contains("READ_ACCOUNT");
  }
}
