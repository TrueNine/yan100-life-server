package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.RefId
import net.yan100.compose.rds.IRepo
import net.yan100.life.infrastructure.jimmer.entities.UserAccount
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Primary
@Repository
interface IUserAccountRepo : IRepo<UserAccount, RefId> {
  fun findByAccount(account: String): UserAccount?
  fun findByPhone(phone: String): UserAccount?
  fun findByWechatWxpaOpenId(wechatWxpaOpenId: String): UserAccount?
  fun existsByAccount(account: String): Boolean
  fun existsByPhone(phone: String): Boolean
}
