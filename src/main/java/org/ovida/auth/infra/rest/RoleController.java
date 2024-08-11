package org.ovida.auth.infra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ovida.auth.app.role.RoleAppSvc;
import org.ovida.auth.app.role.UpdateRoleCmd;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/roles")
class RoleController {

  private final RoleAppSvc roleAppSvc;

  @PatchMapping("/{roleName}")
  public void update(
      @PathVariable String roleName,
      @Valid @RequestBody UpdateRoleRequest request) {
    var cmd = new UpdateRoleCmd()
      .setRoleName(roleName)
      .setPrivilegeNames(request.getPrivilegeNames());

    roleAppSvc.update(cmd);
  }
}
