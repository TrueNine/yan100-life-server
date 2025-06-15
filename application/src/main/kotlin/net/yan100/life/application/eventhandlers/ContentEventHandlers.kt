package net.yan100.life.application.eventhandlers

import net.yan100.life.domain.content.*
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ContentEventHandlers {

  private val logger = LoggerFactory.getLogger(javaClass)

  @EventListener
  suspend fun handle(event: PostContentCreatedEvent) {
    logger.info("帖子创建: postId=${event.aggregateId}, title=${event.title}")
    // TODO: 通知审核人员
    // TODO: 更新用户发帖统计
  }

  @EventListener
  suspend fun handle(event: PostContentUpdatedEvent) {
    logger.info("帖子更新: postId=${event.aggregateId}")
    // TODO: 清除缓存
    // TODO: 重新进入审核流程
  }

  @EventListener
  suspend fun handle(event: PostContentSubmittedForAuditEvent) {
    logger.info("帖子提交审核: postId=${event.aggregateId}")
    // TODO: 加入审核队列
    // TODO: 通知审核人员
  }

  @EventListener
  suspend fun handle(event: PostContentApprovedEvent) {
    logger.info("帖子审核通过: postId=${event.aggregateId}, auditorId=${event.auditorId}")
    // TODO: 通知作者
    // TODO: 发布到首页
    // TODO: 更新搜索索引
  }

  @EventListener
  suspend fun handle(event: PostContentRejectedEvent) {
    logger.info("帖子审核拒绝: postId=${event.aggregateId}, reason=${event.reason}")
    // TODO: 通知作者拒绝原因
  }

  @EventListener
  suspend fun handle(event: PostContentRemovedEvent) {
    logger.info("帖子删除: postId=${event.aggregateId}")
    // TODO: 清除缓存
    // TODO: 更新搜索索引
    // TODO: 通知收藏该帖子的用户
  }
} 
