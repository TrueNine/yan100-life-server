package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot
import net.yan100.life.domain.DomainEvent

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

/** 权限创建事件 */
data class PermissionCreatedEvent(
  override val aggregateId: AggregateId,
  val code: String,
  val name: String,
) : DomainEvent(aggregateId = aggregateId)

/** 权限更新事件 */
data class PermissionUpdatedEvent(
  override val aggregateId: AggregateId.Change,
  val code: String,
  val name: String,
) : DomainEvent(aggregateId = aggregateId) 
