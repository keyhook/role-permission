package org.ovida.auth.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import org.junit.Test;
import org.ovida.auth.JpaSvcAbstract_IT;
import org.ovida.auth.app.role.StdRoleAppSvc;
import org.ovida.auth.app.role.UpdateRoleCmd;
import org.ovida.auth.domain.model.RolePrivilege;
import org.ovida.auth.domain.model.RolePrivilege.RolePrivilegeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

public class StdRoleAppSvc_IT extends JpaSvcAbstract_IT {

  @Autowired
  private StdRoleAppSvc stdRoleAppSvc;

  @Test
  @Sql("StdRoleAppSvc_Case.sql")
  public void shouldAddNewPrivilegesToRole() {
    var emptyRolePrivileges = jpaTestHelper.getNestedResults("rolePrivilegeId", "roleName",
      "Member", RolePrivilege.class);

    assertThat(emptyRolePrivileges).isEmpty();

    var newPrivileges = Set.of("READ_ACCOUNT", "WRITE_ROLE");

    var cmd = new UpdateRoleCmd()
      .setRoleName("Member")
      .setPrivilegeNames(newPrivileges);

    stdRoleAppSvc.update(cmd);

    var rolePrivileges = jpaTestHelper.getNestedResults("rolePrivilegeId", "roleName",
      "Member", RolePrivilege.class);

    assertThat(rolePrivileges)
      .extracting(RolePrivilege::getRolePrivilegeId)
      .extracting(RolePrivilegeId::getPrivilegeName)
      .containsAll(newPrivileges);
  }

  @Test
  @Sql("StdRoleAppSvc_Case.sql")
  public void shouldRemoveExistingPrivilegesFromRole() {
    var existingRolePrivileges = jpaTestHelper.getNestedResults("rolePrivilegeId", "roleName",
      "Admin", RolePrivilege.class);

    assertThat(existingRolePrivileges).isNotEmpty();

    var cmd = new UpdateRoleCmd()
      .setRoleName("Admin")
      .setPrivilegeNames(Set.of());

    stdRoleAppSvc.update(cmd);

    var emptyRolePrivileges = jpaTestHelper.getNestedResults("rolePrivilegeId", "roleName",
      "Admin", RolePrivilege.class);

    assertThat(emptyRolePrivileges).isEmpty();
  }

  @Test
  @Sql("StdRoleAppSvc_Case.sql")
  public void shouldNotAddUnknownPrivileges() {
    var cmd = new UpdateRoleCmd()
      .setRoleName("Member")
      .setPrivilegeNames(Set.of("UNKNOWN_PRIVILEGE"));

    assertThatThrownBy(() -> stdRoleAppSvc.update(cmd))
      .isInstanceOf(DataIntegrityViolationException.class);
  }
}
