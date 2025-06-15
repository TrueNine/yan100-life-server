package net.yan100.life.domain.content

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEvent
import net.yan100.life.domain.enums.PostMessageType

/**
 * 帖子内容创建事件
 */
data class PostContentCreatedEvent(
  override val aggregateId: AggregateId.Create,
  val title: String,
  val type: PostMessageType,
  val pubUserAccountId: AggregateId.Change,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 帖子内容更新事件
 */
data class PostContentUpdatedEvent(
  override val aggregateId: AggregateId.Change,
  val title: String? = null,
  val content: String? = null,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 帖子提交审核事件
 */
data class PostContentSubmittedForAuditEvent(
  override val aggregateId: AggregateId.Change,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 帖子审核通过事件
 */
data class PostContentApprovedEvent(
  override val aggregateId: AggregateId.Change,
  val auditorId: AggregateId.Change,
) : DomainEvent(
  aggregateId = aggregateId
)

/**
 * 帖子审核拒绝事件
 */
data class PostContentRejectedEvent(
  override val aggregateId: AggregateId.Change,
  val auditorId: AggregateId.Change,
  val reason: String,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 帖子删除事件
 */
data class PostContentRemovedEvent(
  override val aggregateId: AggregateId.Change,
) : DomainEvent(aggregateId = aggregateId) 
