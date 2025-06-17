package net.yan100.life.domain.rbac

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEvent

/** 用户分配角色组事件 */
data class UserRoleGroupAssignedEvent(
  override val aggregateId: AggregateId,
  val roleGroupId: AggregateId,
) : DomainEvent(aggregateId = aggregateId)

/** 用户移除角色组事件 */
data class UserRoleGroupRemovedEvent(
  override val aggregateId: AggregateId,
  val roleGroupId: AggregateId,
) : DomainEvent(aggregateId = aggregateId)

/** 用户撤销权限事件 */
data class UserPermissionRevokedEvent(
  override val aggregateId: AggregateId,
  val permissionId: AggregateId,
) : DomainEvent(aggregateId = aggregateId)

/** 用户恢复权限事件 */
data class UserPermissionRestoredEvent(
  override val aggregateId: AggregateId,
  val permissionId: AggregateId,
) : DomainEvent(aggregateId = aggregateId) 