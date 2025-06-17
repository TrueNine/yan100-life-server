package net.yan100.life.infrastructure.jimmer.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.toAggregateChangeId
import net.yan100.life.domain.user.UserAccountAggregate
import net.yan100.life.domain.user.UserAccountRepository
import net.yan100.life.infrastructure.jimmer.entities.*
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserAccountRepositoryImpl(
  private val sqlClient: KSqlClient,
  private val userRepo: IUserAccountRepo,
) : UserAccountRepository {

  override suspend fun existsById(id: AggregateId.Query): Boolean {
    return userRepo.existsById(id.id)
  }

  override suspend fun findById(id: AggregateId.Query): UserAccountAggregate? {
    return userRepo.findByIdOrNull(id.id)?.toAggregate()
  }

  override suspend fun findByAccount(account: String): UserAccountAggregate? = withContext(Dispatchers.IO) {
    userRepo.findByAccount(account)?.toAggregate()
  }

  override suspend fun findByPhone(phone: String): UserAccountAggregate? = withContext(Dispatchers.IO) {
    userRepo.findByPhone(phone)?.toAggregate()
  }

  override suspend fun findByWechatOpenId(openId: String): UserAccountAggregate? = withContext(Dispatchers.IO) {
    userRepo.findByWechatWxpaOpenId(openId)?.toAggregate()
  }

  override suspend fun save(aggregate: UserAccountAggregate): UserAccountAggregate = withContext(Dispatchers.IO) {
    val entity = aggregate.toEntity()
    val saved = sqlClient.save(entity) {
      setMode(SaveMode.UPSERT)
    }
    saved.modifiedEntity.toAggregate()
  }

  override suspend fun existsByAccount(account: String): Boolean = withContext(Dispatchers.IO) {
    userRepo.existsByAccount(account)
  }

  override suspend fun existsByPhone(phone: String): Boolean = withContext(Dispatchers.IO) {
    userRepo.existsByPhone(phone)
  }

  private fun UserAccount.toAggregate(): UserAccountAggregate {
    return UserAccountAggregate(
      id = id.toAggregateChangeId(),
      account = account,
      passwordEnc = passwordEnc,
      nickName = nickName,
      phone = phone,
      avatarUrl = avatarUrl,
      wechatWxpaOpenId = wechatWxpaOpenId,
      favoritePostContentIds = favoritePostContents.map { it.id.toAggregateChangeId() }.toMutableSet()
    )
  }

  private fun UserAccountAggregate.toEntity(): UserAccount {
    return UserAccount {
      this.id = this@toEntity.id.toQueryId().id
      account = this@toEntity.account
      passwordEnc = this@toEntity.passwordEnc
      nickName = this@toEntity.nickName
      phone = this@toEntity.phone
      avatarUrl = this@toEntity.avatarUrl
      wechatWxpaOpenId = this@toEntity.wechatWxpaOpenId
      favoritePostContents = getFavoritePostIds().map {
        PostContent {
          id = it.toQueryId().id
        }
      }
    }
  }
}
