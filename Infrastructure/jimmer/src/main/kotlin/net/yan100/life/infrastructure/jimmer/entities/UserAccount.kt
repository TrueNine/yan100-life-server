package net.yan100.life.infrastructure.jimmer.entities

import net.yan100.compose.rds.entities.IEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface UserAccount : IEntity {
  /**
   * 手机号
   */
  val phone: String?

  /**
   * 头像 url
   */
  val avatarUrl: String?


  /**
   * 微信公众号OpenId
   */
  val wechatWxpaOpenId: String?

  val account: String

  val passwordEnc: String

  val nickName: String

  @ManyToMany
  val favoritePostContents: List<PostContent>

  @ManyToMany
  val roleGroups: List<RoleGroup>
}
