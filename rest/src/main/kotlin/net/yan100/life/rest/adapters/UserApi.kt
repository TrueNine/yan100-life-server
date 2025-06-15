package net.yan100.life.rest.adapters

import net.yan100.compose.toId
import net.yan100.life.application.CommandBus
import net.yan100.life.application.QueryBus
import net.yan100.life.application.user.commands.*
import net.yan100.life.application.user.queries.GetUserDetailQuery
import net.yan100.life.domain.toAggregateChangeId
import net.yan100.life.rest.dto.*
import org.springframework.web.bind.annotation.*

/**
 * 用户账户接口
 */
@RestController
@RequestMapping("v1/users")
class UserApi(
  private val commandBus: CommandBus,
  private val queryBus: QueryBus,
) {

  /**
   * 注册用户
   */
  @PostMapping("register")
  suspend fun register(@RequestBody request: RegisterDto): RegisterView {
    val userId = commandBus.send(
      CreateUserAccountCommand(
        account = request.account,
        password = request.password,
        nickName = request.nickName,
        phone = request.phone
      )
    )
    return RegisterView(userId = userId.toString())
  }

  /**
   * 获取用户详情
   */
  @GetMapping("{userId}")
  suspend fun getUserDetail(@PathVariable userId: String): UserDetailView? {
    val user = queryBus.send(GetUserDetailQuery(userId.toId()!!))
    return user?.let {
      UserDetailView(
        id = it.id,
        account = it.account,
        nickName = it.nickName,
        phone = it.phone,
        avatarUrl = it.avatarUrl,
        hasWechatBound = it.hasWechatBound,
        favoritePostIds = it.favoritePostIds,
      )
    }
  }

  /**
   * 更新用户资料
   */
  @PutMapping("{userId}/profile")
  suspend fun updateProfile(
    @PathVariable userId: String,
    @RequestBody request: UpdateProfileDto,
  ) {
    commandBus.send(
      UpdateUserProfileCommand(
        userId = userId.toAggregateChangeId(),
        nickName = request.nickName,
        phone = request.phone,
        avatarUrl = request.avatarUrl
      )
    )
  }

  /**
   * 修改密码
   */
  @PostMapping("{userId}/change_password")
  suspend fun changePassword(
    @PathVariable userId: String,
    @RequestBody request: ChangePasswordDto,
  ) {
    commandBus.send(
      ChangePasswordCommand(
        userId = userId.toAggregateChangeId(),
        oldPassword = request.oldPassword,
        newPassword = request.newPassword
      )
    )
  }

  /**
   * 绑定微信 微信公众号
   */
  @PostMapping("{userId}/bind_wechat_wxpa")
  suspend fun bindWechat(
    @PathVariable userId: String,
    @RequestBody request: BindWechatDto,
  ) {
    commandBus.send(
      BindWechatCommand(
        userId = userId.toAggregateChangeId(),
        openId = request.openId
      )
    )
  }

  /**
   * 收藏帖子
   */
  @PostMapping("{userId}/favorites/{postId}")
  suspend fun favoritePost(
    @PathVariable userId: String,
    @PathVariable postId: String,
  ) {
    commandBus.send(
      FavoritePostCommand(
        userId = userId.toAggregateChangeId(),
        postId = postId.toAggregateChangeId()
      )
    )
  }

  /**
   * 取消收藏
   */
  @DeleteMapping("{userId}/favorites/{postId}")
  suspend fun unfavoritePost(
    @PathVariable userId: String,
    @PathVariable postId: String,
  ) {
    commandBus.send(
      UnfavoritePostCommand(
        userId = userId.toAggregateChangeId(),
        postId = postId.toAggregateChangeId()
      )
    )
  }
}
