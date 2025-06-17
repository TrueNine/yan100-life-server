package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot

/**
 * 权限聚合根
 */
class PermissionAggregate(
  override var id: AggregateId,
  var code: String, // 权限唯一标识
  var name: String, // 权限名称
  var description: String? = null,
) : AggregateRoot(id) {
  companion object {
    fun create(code: String, name: String, description: String? = null): PermissionAggregate {
      val id = AggregateId.create()
      val permission = PermissionAggregate(id, code, name, description)
      permission.raiseEvent(PermissionCreatedEvent(id, code, name))
      return permission
    }
  }

  fun update(name: String? = null, description: String? = null) {
    name?.also { this.name = it }
    description?.also { this.description = it }
    raiseEvent(PermissionUpdatedEvent(id.toChangeId(), this.code, this.name))
  }
} 
