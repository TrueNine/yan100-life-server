package net.yan100.life.application.eventhandlers

import net.yan100.compose.slf4j
import net.yan100.life.domain.user.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserEventHandlers {

  companion object {
    @JvmStatic
    private val log = slf4j<UserEventHandlers>()
  }

  @EventListener
  suspend fun handle(event: UserAccountCreatedEvent) {
    log.info("[handle] user account created userId: {} , account: {}", event.aggregateId, event.account)
    // TODO: 发送欢迎邮件或短信
    // TODO: 初始化用户配置
  }

  @EventListener
  suspend fun handle(event: UserProfileUpdatedEvent) {
    log.info("[handle] user profile updated userId: {}", event.aggregateId)
    // TODO: 同步更新缓存
    // TODO: 通知相关服务
  }

  @EventListener
  suspend fun handle(event: WechatBoundEvent) {
    log.info("[handle] wechat bound userId: {} , openId: {}", event.aggregateId, event.openId)
    // TODO: 同步微信用户信息
  }

  @EventListener
  suspend fun handle(event: PasswordChangedEvent) {
    log.info("[handle] password changed userId: {}", event.aggregateId)
    // TODO: 发送密码修改通知
    // TODO: 强制其他登录设备下线
  }

  @EventListener
  suspend fun handle(event: PostFavoritedEvent) {
    log.info("[handle] post favorited userId: {} , postId: {}", event.aggregateId, event.postId)
    // TODO: 更新帖子收藏计数
    // TODO: 通知帖子作者
  }

  @EventListener
  suspend fun handle(event: PostUnfavoritedEvent) {
    log.info("[handle] post unfavorited userId: {} , postId: {}", event.aggregateId, event.postId)
    // TODO: 更新帖子收藏计数
  }
} 
