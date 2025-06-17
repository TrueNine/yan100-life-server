package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEvent

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