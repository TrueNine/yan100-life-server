package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot

/**
 * 角色聚合根
 */
class RoleAggregate(
  override var id: AggregateId,
  var code: String,
  var name: String,
  var description: String? = null,
  private val permissionIds: MutableSet<AggregateId> = mutableSetOf()
) : AggregateRoot(id) {
  companion object {
    @JvmStatic
    fun create(code: String, name: String, description: String? = null): RoleAggregate {
      require(code.isNotBlank()) { "code must not be blank" }
      require(name.isNotBlank()) { "name must not be blank" }
      val id = AggregateId.create()
      val role = RoleAggregate(id, code, name, description)
      role.raiseEvent(RoleCreatedEvent(id, code, name))
      return role
    }
  }

  fun addPermission(permissionId: AggregateId) {
    if (permissionIds.add(permissionId)) {
      raiseEvent(RolePermissionAddedEvent(id, permissionId))
    }
  }

  fun removePermission(permissionId: AggregateId) {
    if (permissionIds.remove(permissionId)) {
      raiseEvent(RolePermissionRemovedEvent(id, permissionId))
    }
  }

  fun getPermissionIds(): Set<AggregateId> = permissionIds.toSet()
} 
