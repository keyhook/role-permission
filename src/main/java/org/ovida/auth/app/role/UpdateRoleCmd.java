package org.ovida.auth.app.role;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UpdateRoleCmd {

  private String roleName;

  private Set<String> privilegeNames;
}
