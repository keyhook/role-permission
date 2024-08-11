package org.ovida.auth.infra.rest;

import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UpdateRoleRequest {

  @Size(max = 50)
  private Set<String> privilegeNames = Set.of();
}
