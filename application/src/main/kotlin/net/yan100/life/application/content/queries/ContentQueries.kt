package net.yan100.life.application.content.queries

import net.yan100.compose.Pq
import net.yan100.compose.Pr
import net.yan100.compose.RefId
import net.yan100.life.application.Query
import net.yan100.life.application.dto.AuditDto
import net.yan100.life.application.dto.MessageDto
import net.yan100.life.application.dto.PostDto
import net.yan100.life.application.dto.PostListItemView
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType

/**
 * 获取帖子详情查询
 */
data class GetPostDetailQuery(
  val postId: RefId,
) : Query<PostDto?>

/**
 * 获取帖子列表查询
 */
data class GetPostsQuery(
  val status: PostContentStatus? = null,
  val type: PostMessageType? = null,
  val pq: Pq = Pq.DEFAULT_MAX
) : Query<Pr<PostListItemView>>

/**
 * 获取用户帖子查询
 */
data class GetUserPostsQuery(
  val userId: RefId,
  val status: PostContentStatus? = null,
  val pq: Pq = Pq.DEFAULT_MAX
) : Query<Pr<PostListItemView>>

/**
 * 获取待审核帖子查询
 */
data class GetPendingAuditPostsQuery(
  val pq: Pq = Pq.DEFAULT_MAX
) : Query<Pr<PostListItemView>>

/**
 * 获取用户消息查询
 */
data class GetUserMessagesQuery(
  val userId: RefId,
  val unreadOnly: Boolean = false,
  val pq: Pq = Pq.DEFAULT_MAX
) : Query<Pr<MessageDto>>

/**
 * 获取审核记录查询
 */
data class GetAuditRecordsQuery(
  val postId: RefId? = null,
  val auditorId: RefId? = null,
  val pq: Pq = Pq.DEFAULT_MAX
) : Query<Pr<AuditDto>>
