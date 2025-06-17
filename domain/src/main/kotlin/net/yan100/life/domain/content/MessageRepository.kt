package net.yan100.life.domain.content

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.IDomainRepository

/**
 * 消息仓储接口
 */
interface MessageRepository : IDomainRepository<MessageAggregate> {
  
  /**
   * 根据接收用户ID查找消息
   */
  suspend fun findByToUserId(toUserId: AggregateId.Query): List<MessageAggregate>
  
  /**
   * 根据发送用户ID查找消息
   */
  suspend fun findByFromUserId(fromUserId: AggregateId.Query): List<MessageAggregate>
  
  /**
   * 根据帖子ID查找消息
   */
  suspend fun findByPostId(postId: AggregateId.Query): List<MessageAggregate>
} 