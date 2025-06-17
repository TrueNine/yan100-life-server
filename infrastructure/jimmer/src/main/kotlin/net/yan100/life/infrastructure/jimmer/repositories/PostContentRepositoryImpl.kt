package net.yan100.life.infrastructure.jimmer.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.yan100.compose.Pq
import net.yan100.compose.Pr
import net.yan100.compose.RefId
import net.yan100.compose.rds.entities.crd
import net.yan100.compose.rds.fetchPq
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.content.PostContentAggregate
import net.yan100.life.domain.content.PostContentRepository
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import net.yan100.life.domain.toAggregateChangeId
import net.yan100.life.infrastructure.jimmer.entities.*
import net.yan100.life.infrastructure.jimmer.toAggregateRootResultId
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.desc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.springframework.stereotype.Repository

@Repository
class PostContentRepositoryImpl(
  private val sqlClient: KSqlClient,
  private val postRepo: IPostContentRepo,
) : PostContentRepository {
  override suspend fun existsById(id: AggregateId.Query): Boolean {
    return postRepo.existsById(id.id)
  }

  override suspend fun findById(id: AggregateId.Query): PostContentAggregate? = withContext(Dispatchers.IO) {
    sqlClient
      .findById(PostContent::class, id)
      ?.toAggregate()
  }

  override suspend fun save(aggregate: PostContentAggregate): PostContentAggregate = withContext(Dispatchers.IO) {
    val entity = aggregate.toEntity()
    val saved = sqlClient.save(entity) {
      setMode(SaveMode.UPSERT)
    }
    saved.modifiedEntity.toAggregate()
  }

  override suspend fun findByStatus(
    status: PostContentStatus,
    pq: Pq
  ): Pr<PostContentAggregate> = withContext(Dispatchers.IO) {
    sqlClient
      .createQuery(PostContent::class) {
        where(table.status eq status)
        orderBy(table.crd.desc())
        select(table)
      }
      .fetchPq(pq) { it.toAggregate() }
  }

  override suspend fun findByUserAndStatus(
    userId: RefId,
    status: PostContentStatus?,
    pq: Pq
  ): Pr<PostContentAggregate> = withContext(Dispatchers.IO) {
    sqlClient
      .createQuery(PostContent::class) {
        where(table.pubUserAccountId eq userId)
        status?.also {
          where(table.status eq it)
        }
        orderBy(table.crd.desc())
        select(table)
      }
      .fetchPq(pq) { it.toAggregate() }
  }

  override suspend fun findByType(
    type: PostMessageType,
    pq: Pq
  ): Pr<PostContentAggregate> = withContext(Dispatchers.IO) {
    sqlClient
      .createQuery(PostContent::class) {
        where(table.type eq type)
        where(table.status eq PostContentStatus.AUDIT_PASS)
        orderBy(table.crd.desc())
        select(table)
      }
      .fetchPq(pq) { it.toAggregate() }
  }

  override suspend fun countByStatus(status: PostContentStatus): Long = withContext(Dispatchers.IO) {
    sqlClient
      .createQuery(PostContent::class) {
        where(table.status eq status)
        select(count(table))
      }
      .fetchOne()
  }

  // 转换扩展函数
  private fun PostContent.toAggregate(): PostContentAggregate {
    val primaryId = this.toAggregateRootResultId()

    return PostContentAggregate(
      id = primaryId,
      title = title,
      content = content,
      type = type,
      status = status,
      pubUserAccountId = pubUserAccountId?.toAggregateChangeId()!!
    )
  }

  private fun PostContentAggregate.toEntity(): PostContent {
    return PostContent {
      title = this@toEntity.title
      content = this@toEntity.content
      type = this@toEntity.type
      status = this@toEntity.status
    }
  }
}
