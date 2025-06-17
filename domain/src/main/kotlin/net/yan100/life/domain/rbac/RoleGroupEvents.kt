package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEvent

/** 角色组创建事件 */
data class RoleGroupCreatedEvent(
  override val aggregateId: AggregateId,
  val code: String,
  val name: String
) : DomainEvent(aggregateId = aggregateId)

/** 角色组添加角色事件 */
data class RoleGroupRoleAddedEvent(
  override val aggregateId: AggregateId,
  val roleId: AggregateId
) : DomainEvent(aggregateId = aggregateId)

/** 角色组移除角色事件 */
data class RoleGroupRoleRemovedEvent(
  override val aggregateId: AggregateId,
  val roleId: AggregateId
) : DomainEvent(aggregateId = aggregateId) 