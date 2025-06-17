package net.yan100.life.application.eventhandlers

import net.yan100.compose.slf4j
import net.yan100.life.domain.content.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ContentEventHandlers {
  companion object {
    @JvmStatic
    private val log = slf4j<ContentEventHandlers>()
  }

  @EventListener
  suspend fun handle(event: PostContentCreatedEvent) {
    log.info("[handle] post created postId: {} , title: {}", event.aggregateId, event.title)
    // TODO: 通知审核人员
    // TODO: 更新用户发帖统计
  }

  @EventListener
  suspend fun handle(event: PostContentUpdatedEvent) {
    log.info("[handle] post updated postId: {}", event.aggregateId)
    // TODO: 清除缓存
    // TODO: 重新进入审核流程
  }

  @EventListener
  suspend fun handle(event: PostContentSubmittedForAuditEvent) {
    log.info("[handle] post submitted for audit postId: {}", event.aggregateId)
    // TODO: 加入审核队列
    // TODO: 通知审核人员
  }

  @EventListener
  suspend fun handle(event: PostContentApprovedEvent) {
    log.info("[handle] post approved postId: {} , auditorId: {}", event.aggregateId, event.auditorId)
    // TODO: 通知作者
    // TODO: 发布到首页
    // TODO: 更新搜索索引
  }

  @EventListener
  suspend fun handle(event: PostContentRejectedEvent) {
    log.info("[handle] post rejected postId: {} , reason: {}", event.aggregateId, event.reason)
    // TODO: 通知作者拒绝原因
  }

  @EventListener
  suspend fun handle(event: PostContentRemovedEvent) {
    log.info("[handle] post removed postId: {}", event.aggregateId)
    // TODO: 清除缓存
    // TODO: 更新搜索索引
    // TODO: 通知收藏该帖子的用户
  }
} 
