package net.yan100.life.domain.content

import net.yan100.compose.Pq
import net.yan100.compose.Pr
import net.yan100.compose.RefId
import net.yan100.life.domain.IDomainRepository
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType

/**
 * 帖子内容仓储接口
 */
interface PostContentRepository : IDomainRepository<PostContentAggregate> {
  suspend fun findByStatus(status: PostContentStatus, pq: Pq = Pq.DEFAULT_MAX): Pr<PostContentAggregate>
  suspend fun findByUserAndStatus(userId: RefId, status: PostContentStatus?, pq: Pq = Pq.DEFAULT_MAX): Pr<PostContentAggregate>
  suspend fun findByType(type: PostMessageType, pq: Pq = Pq.DEFAULT_MAX): Pr<PostContentAggregate>
  suspend fun countByStatus(status: PostContentStatus): Long
} 
