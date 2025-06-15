package net.yan100.life.domain.content

import net.yan100.compose.RefId
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import net.yan100.life.domain.IDomainRepository

/**
 * 帖子内容仓储接口
 */
interface PostContentRepository : IDomainRepository<PostContentAggregate> {
  suspend fun findByStatus(status: PostContentStatus, page: Int, size: Int): List<PostContentAggregate>
  suspend fun findByUserAndStatus(userId: RefId, status: PostContentStatus?, page: Int, size: Int): List<PostContentAggregate>
  suspend fun findByType(type: PostMessageType, page: Int, size: Int): List<PostContentAggregate>
  suspend fun countByStatus(status: PostContentStatus): Long
} 
