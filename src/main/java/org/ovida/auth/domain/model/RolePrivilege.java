package org.ovida.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "rolePrivilegeId")
@ToString
@Entity
@Table(name = "role_privilege")
public class RolePrivilege {

  @EmbeddedId
  @JsonUnwrapped
  private RolePrivilegeId rolePrivilegeId;

  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt = LocalDateTime.now();

  private RolePrivilege() {
  }

  public RolePrivilege(String roleName, String privilegeName) {
    rolePrivilegeId = new RolePrivilegeId()
      .setRoleName(roleName)
      .setPrivilegeName(privilegeName);

  }

  @Accessors(chain = true)
  @Getter
  @Setter
  @EqualsAndHashCode
  @Embeddable
  @ToString
  public static class RolePrivilegeId implements Serializable {

    private String roleName;

    private String privilegeName;
  }
}
