package net.yan100.life.domain.user

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEvent

/**
 * 用户账户创建事件
 */
data class UserAccountCreatedEvent(
  override val aggregateId: AggregateId.Create,
  val account: String,
  val nickName: String,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 用户资料更新事件
 */
data class UserProfileUpdatedEvent(
  override val aggregateId: AggregateId.Change,
  val nickName: String? = null,
  val phone: String? = null,
  val avatarUrl: String? = null,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 微信绑定事件
 */
data class WechatBoundEvent(
  override val aggregateId: AggregateId.Change,
  val openId: String,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 密码修改事件
 */
data class PasswordChangedEvent(
  override val aggregateId: AggregateId.Change,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 帖子收藏事件
 */
data class PostFavoritedEvent(
  override val aggregateId: AggregateId.Change,
  val postId: AggregateId.Change,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 取消帖子收藏事件
 */
data class PostUnfavoritedEvent(
  override val aggregateId: AggregateId.Change,
  val postId: AggregateId.Change,
) : DomainEvent(aggregateId = aggregateId)

/**
 * 用户权限初始化事件
 */
data class UserPermissionInitializedEvent(
  override val aggregateId: AggregateId.Create,
) : DomainEvent(aggregateId = aggregateId) 
