package net.yan100.life.application.content.commands

import net.yan100.life.application.Command
import net.yan100.life.application.CommandWithResult
import net.yan100.life.domain.enums.PostMessageType
import net.yan100.life.domain.AggregateId

/**
 * 创建帖子命令
 */
data class CreatePostCommand(
  val title: String,
  val content: String,
  val type: PostMessageType,
  val pubUserAccountId: AggregateId.Change,
) : CommandWithResult<AggregateId.Result>

/**
 * 更新帖子命令
 */
data class UpdatePostCommand(
  val postId: AggregateId.Change,
  val title: String? = null,
  val content: String? = null,
  val userId: AggregateId.Change,
) : Command

/**
 * 提交审核命令
 */
data class SubmitPostForAuditCommand(
  val postId: AggregateId.Change,
  val userId: AggregateId.Change,
) : Command

/**
 * 审核通过命令
 */
data class ApprovePostCommand(
  val postId: AggregateId.Change,
  val auditorId: AggregateId.Change,
) : Command

/**
 * 审核拒绝命令
 */
data class RejectPostCommand(
  val postId: AggregateId.Change,
  val auditorId: AggregateId.Change,
  val reason: String,
) : Command

/**
 * 删除帖子命令
 */
data class RemovePostCommand(
  val postId: AggregateId.Change,
  val userId: AggregateId.Change,
) : Command

/**
 * 发送消息命令
 */
data class SendMessageCommand(
  val fromUserId: AggregateId,
  val toUserId: AggregateId,
  val postId: AggregateId,
  val content: String,
) : CommandWithResult<AggregateId.Result>
