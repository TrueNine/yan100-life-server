package net.yan100.life.application.user.commands

import net.yan100.life.application.Command
import net.yan100.life.application.CommandWithResult
import net.yan100.life.domain.AggregateId

/**
 * 创建用户账户命令
 */
data class CreateUserAccountCommand(
  val account: String,
  val password: String,
  val nickName: String,
  val phone: String? = null,
) : CommandWithResult<AggregateId>

/**
 * 更新用户资料命令
 */
data class UpdateUserProfileCommand(
  val userId: AggregateId.Change,
  val nickName: String? = null,
  val phone: String? = null,
  val avatarUrl: String? = null,
) : Command

/**
 * 绑定微信命令
 */
data class BindWechatCommand(
  val userId: AggregateId.Change,
  val openId: String,
) : Command

/**
 * 修改密码命令
 */
data class ChangePasswordCommand(
  val userId: AggregateId.Change,
  val oldPassword: String,
  val newPassword: String,
) : Command

/**
 * 收藏帖子命令
 */
data class FavoritePostCommand(
  val userId: AggregateId.Change,
  val postId: AggregateId.Change,
) : Command

/**
 * 取消收藏帖子命令
 */
data class UnfavoritePostCommand(
  val userId: AggregateId.Change,
  val postId: AggregateId.Change,
) : Command
