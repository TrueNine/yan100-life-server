package net.yan100.life.application.dto

import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType

/**
 * 用户信息DTO
 */
data class UserView(
  val id: String,
  val account: String,
  val nickName: String,
  val phone: String? = null,
  val avatarUrl: String? = null,
  val hasWechatBound: Boolean = false,
)

/**
 * 用户详情DTO
 */
data class UserDetailDto(
  val id: String,
  val account: String,
  val nickName: String,
  val phone: String? = null,
  val avatarUrl: String? = null,
  val hasWechatBound: Boolean = false,
  val favoritePostIds: List<String> = emptyList(),
)

/**
 * 帖子DTO
 */
data class PostDto(
  val id: String,
  val title: String,
  val content: String,
  val type: PostMessageType,
  val status: PostContentStatus,
  val publisher: UserView,
)

/**
 * 帖子列表项DTO
 */
data class PostListItemView(
  val id: String,
  val title: String,
  val type: PostMessageType,
  val status: PostContentStatus,
  val publisherName: String,
  val publisherId: String,
)

/**
 * 消息DTO
 */
data class MessageDto(
  val id: String,
  val content: String,
  val fromUser: UserView,
  val toUser: UserView,
  val post: PostListItemView,
  val read: Boolean,
)

/**
 * 审核记录DTO
 */
data class AuditDto(
  val id: String,
  val postId: String,
  val postTitle: String,
  val auditor: UserView,
  val status: PostContentStatus,
  val reason: String? = null,
)

/**
 * 分页结果DTO
 */
data class PageDto<T>(
  val content: List<T>,
  val pageNumber: Int,
  val pageSize: Int,
  val totalElements: Long,
  val totalPages: Int,
)
