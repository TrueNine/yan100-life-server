package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot

/**
 * 用户权限聚合根
 * 用户只能拥有角色组，不能直接拥有角色或权限
 * 用户可以撤销（放弃）自己的权限
 */
class UserPermissionAggregate(
  override var id: AggregateId, // 用户ID
  private val roleGroupIds: MutableSet<AggregateId> = mutableSetOf(),
  private val revokedPermissionIds: MutableSet<AggregateId> = mutableSetOf(),
) : AggregateRoot(id) {
  companion object {
    @JvmStatic
    fun create(userId: AggregateId.Create): UserPermissionAggregate {
      return UserPermissionAggregate(userId)
    }
  }

  fun assignRoleGroup(roleGroupId: AggregateId) {
    if (!roleGroupIds.add(roleGroupId)) return
    raiseEvent(UserRoleGroupAssignedEvent(id, roleGroupId))
  }

  fun removeRoleGroup(roleGroupId: AggregateId) {
    if (roleGroupIds.remove(roleGroupId)) return
    raiseEvent(UserRoleGroupRemovedEvent(id, roleGroupId))
  }

  fun revokePermission(permissionId: AggregateId) {
    if (!revokedPermissionIds.add(permissionId)) return
    raiseEvent(UserPermissionRevokedEvent(id, permissionId))
  }

  fun restorePermission(permissionId: AggregateId) {
    if (!revokedPermissionIds.remove(permissionId)) {
      return
    }
    raiseEvent(UserPermissionRestoredEvent(id, permissionId))
  }

  fun getRoleGroupIds(): Set<AggregateId> = roleGroupIds.toSet()
  fun getRevokedPermissionIds(): Set<AggregateId> = revokedPermissionIds.toSet()
} 
