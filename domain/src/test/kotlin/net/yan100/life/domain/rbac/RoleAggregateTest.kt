package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RoleAggregateTest {
  @Test
  fun `test create role`() {
    val role = RoleAggregate.create("admin", "管理员", "系统管理员")
    assertEquals("admin", role.code)
    assertEquals("管理员", role.name)
    assertEquals("系统管理员", role.description)
  }

  @Test
  fun `test add and remove permission`() {
    val role = RoleAggregate.create("user", "普通用户")
    val perm1 = AggregateId.create()
    val perm2 = AggregateId.create()
    assertNotEquals(perm1, perm2) {
      "perm1 $perm1 perm2 $perm2"
    }
    role.addPermission(perm1)
    role.addPermission(perm2)
    assertTrue(role.getPermissionIds().contains(perm1))
    assertTrue(role.getPermissionIds().contains(perm2))
    role.removePermission(perm1)
    assertFalse(role.getPermissionIds().contains(perm1))
    assertTrue(role.getPermissionIds().contains(perm2))
  }
} 
