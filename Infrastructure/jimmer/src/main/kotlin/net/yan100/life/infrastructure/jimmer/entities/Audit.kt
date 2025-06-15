package net.yan100.life.infrastructure.jimmer.entities

import net.yan100.compose.RefId
import net.yan100.compose.rds.entities.IEntity
import net.yan100.life.domain.enums.PostContentStatus
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.IdView
import org.babyfish.jimmer.sql.ManyToOne

@Entity
interface Audit : IEntity {
  @IdView("postContent")
  val postContentId: RefId

  @ManyToOne
  val postContent: PostContent

  @IdView("auditorUserAccount")
  val auditorUserAccountId: RefId

  @ManyToOne
  val auditorUserAccount: UserAccount

  /**
   * 审核状态
   */
  val status: PostContentStatus

  val reason: String?
}
