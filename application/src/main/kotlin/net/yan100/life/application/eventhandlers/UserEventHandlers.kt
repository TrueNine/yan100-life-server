package net.yan100.life.application.eventhandlers

import net.yan100.life.domain.user.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserEventHandlers {

  private val logger = LoggerFactory.getLogger(javaClass)

  @EventListener
  suspend fun handle(event: UserAccountCreatedEvent) {
    logger.info("用户账户创建: userId=${event.aggregateId}, account=${event.account}")
    // TODO: 发送欢迎邮件或短信
    // TODO: 初始化用户配置
  }

  @EventListener
  suspend fun handle(event: UserProfileUpdatedEvent) {
    logger.info("用户资料更新: userId=${event.aggregateId}")
    // TODO: 同步更新缓存
    // TODO: 通知相关服务
  }

  @EventListener
  suspend fun handle(event: WechatBoundEvent) {
    logger.info("微信绑定成功: userId=${event.aggregateId}, openId=${event.openId}")
    // TODO: 同步微信用户信息
  }

  @EventListener
  suspend fun handle(event: PasswordChangedEvent) {
    logger.info("密码已修改: userId=${event.aggregateId}")
    // TODO: 发送密码修改通知
    // TODO: 强制其他登录设备下线
  }

  @EventListener
  suspend fun handle(event: PostFavoritedEvent) {
    logger.info("帖子收藏: userId=${event.aggregateId}, postId=${event.postId}")
    // TODO: 更新帖子收藏计数
    // TODO: 通知帖子作者
  }

  @EventListener
  suspend fun handle(event: PostUnfavoritedEvent) {
    logger.info("取消收藏: userId=${event.aggregateId}, postId=${event.postId}")
    // TODO: 更新帖子收藏计数
  }
} 
