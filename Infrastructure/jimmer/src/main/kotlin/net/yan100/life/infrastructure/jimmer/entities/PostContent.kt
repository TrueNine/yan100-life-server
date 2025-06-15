package net.yan100.life.infrastructure.jimmer.entities

import net.yan100.compose.RefId
import net.yan100.compose.rds.entities.IEntity
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.IdView
import org.babyfish.jimmer.sql.ManyToOne

@Entity
interface PostContent : IEntity {
  @IdView("pubUserAccount")
  val pubUserAccountId: RefId?

  /**
   * 发布用户账号
   */
  @ManyToOne
  val pubUserAccount: UserAccount?

  /**
   * 消息类型
   */
  val type: PostMessageType

  /**
   * 帖子标题
   */
  val title: String

  /**
   * 帖子内容
   */
  val content: String

  val status: PostContentStatus
}
