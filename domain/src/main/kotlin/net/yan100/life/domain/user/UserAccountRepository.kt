package net.yan100.life.domain.user

import net.yan100.life.domain.IDomainRepository

/**
 * 用户账户仓储接口
 */
interface UserAccountRepository : IDomainRepository<UserAccountAggregate> {
  suspend fun findByAccount(account: String): UserAccountAggregate?
  suspend fun findByPhone(phone: String): UserAccountAggregate?
  suspend fun findByWechatOpenId(openId: String): UserAccountAggregate?
  suspend fun existsByAccount(account: String): Boolean
  suspend fun existsByPhone(phone: String): Boolean
}
