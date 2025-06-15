package net.yan100.life.infrastructure.jimmer.entities

import net.yan100.compose.RefId
import net.yan100.compose.rds.entities.IEntity
import org.babyfish.jimmer.sql.Default
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.IdView
import org.babyfish.jimmer.sql.ManyToOne

@Entity
interface Message : IEntity {

  @IdView("fromUserAccount")
  val fromUserAccountId: RefId?

  @ManyToOne
  val fromUserAccount: UserAccount?

  @IdView("toUserAccount")
  val toUserAccountId: RefId?

  @ManyToOne
  val toUserAccount: UserAccount?

  @IdView("postContent")
  val postContentId: RefId?

  @ManyToOne
  val postContent: PostContent?

  val content: String

  @Default(value = "false")
  val read: Boolean
}
