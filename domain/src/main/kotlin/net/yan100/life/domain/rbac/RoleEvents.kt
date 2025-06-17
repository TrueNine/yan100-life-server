package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEvent

/** 角色创建事件 */
data class RoleCreatedEvent(
  override val aggregateId: AggregateId,
  val code: String,
  val name: String
) : DomainEvent(aggregateId = aggregateId)

/** 角色添加权限事件 */
data class RolePermissionAddedEvent(
  override val aggregateId: AggregateId,
  val permissionId: AggregateId
) : DomainEvent(aggregateId = aggregateId)

/** 角色移除权限事件 */
data class RolePermissionRemovedEvent(
  override val aggregateId: AggregateId,
  val permissionId: AggregateId
) : DomainEvent(aggregateId = aggregateId) 