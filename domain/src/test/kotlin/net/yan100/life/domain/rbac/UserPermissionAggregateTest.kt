package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserPermissionAggregateTest {
  @Test
  fun `should create user permission aggregate`() {
    val userId = AggregateId.Create.Default()
    val userPermission = UserPermissionAggregate.create(userId)
    assertEquals(userId, userPermission.id)
    assertTrue(userPermission.getRoleGroupIds().isEmpty())
    assertTrue(userPermission.getRevokedPermissionIds().isEmpty())
  }

  @Test
  fun `should assign role group`() {
    val userId = AggregateId.Create.Default()
    val userPermission = UserPermissionAggregate.create(userId)
    val roleGroupId = AggregateId.Create.Default()
    userPermission.assignRoleGroup(roleGroupId)
    assertTrue(userPermission.getRoleGroupIds().contains(roleGroupId))
  }

  @Test
  fun `should remove role group`() {
    val userId = AggregateId.Create.Default()
    val userPermission = UserPermissionAggregate.create(userId)
    val roleGroupId = AggregateId.Create.Default()
    userPermission.assignRoleGroup(roleGroupId)
    userPermission.removeRoleGroup(roleGroupId)
    assertFalse(userPermission.getRoleGroupIds().contains(roleGroupId))
  }

  @Test
  fun `should revoke permission`() {
    val userId = AggregateId.Create.Default()
    val userPermission = UserPermissionAggregate.create(userId)
    val permissionId = AggregateId.Create.Default()
    userPermission.revokePermission(permissionId)
    assertTrue(userPermission.getRevokedPermissionIds().contains(permissionId))
  }

  @Test
  fun `should restore permission`() {
    val userId = AggregateId.Create.Default()
    val userPermission = UserPermissionAggregate.create(userId)
    val permissionId = AggregateId.Create.Default()
    userPermission.revokePermission(permissionId)
    userPermission.restorePermission(permissionId)
    assertFalse(userPermission.getRevokedPermissionIds().contains(permissionId))
  }
} 
