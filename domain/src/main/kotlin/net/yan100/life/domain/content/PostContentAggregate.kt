package net.yan100.life.domain.content

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType

/**
 * 帖子内容聚合根
 */
class PostContentAggregate(
  override var id: AggregateId,
  var title: String,
  var content: String,
  var type: PostMessageType,
  var status: PostContentStatus,
  val pubUserAccountId: AggregateId.Change,
) : AggregateRoot(id) {

  companion object {
    fun create(
      title: String,
      content: String,
      type: PostMessageType,
      pubUserAccountId: AggregateId.Change,
    ): PostContentAggregate {
      val postId = AggregateId.Create.Default()
      val post = PostContentAggregate(
        id = postId,
        title = title,
        content = content,
        type = type,
        status = PostContentStatus.PRE_AUDIT,
        pubUserAccountId = pubUserAccountId
      )
      post.raiseEvent(
        PostContentCreatedEvent(
          aggregateId = postId,
          title = title,
          type = type,
          pubUserAccountId = pubUserAccountId
        )
      )
      return post
    }
  }

  fun update(title: String? = null, content: String? = null) {
    require(status == PostContentStatus.PRE_AUDIT || status == PostContentStatus.AUDIT_FAIL) {
      "只有待审核或审核失败的帖子才能修改"
    }

    title?.let { this.title = it }
    content?.let { this.content = it }

    // 修改后重新进入待审核状态
    status = PostContentStatus.PRE_AUDIT

    raiseEvent(
      PostContentUpdatedEvent(
        aggregateId = id.toChangeId(),
        title = title,
        content = content
      )
    )
  }

  fun submitForAudit() {
    require(status == PostContentStatus.PRE_AUDIT || status == PostContentStatus.AUDIT_FAIL) {
      "只有待审核或审核失败的帖子才能提交审核"
    }
    status = PostContentStatus.PRE_AUDIT
    raiseEvent(PostContentSubmittedForAuditEvent(id.toChangeId()))
  }

  fun approve(auditorId: AggregateId.Change) {
    require(status == PostContentStatus.PRE_AUDIT) { "只有待审核的帖子才能审核通过" }
    status = PostContentStatus.AUDIT_PASS
    raiseEvent(PostContentApprovedEvent(id.toChangeId(), auditorId))
  }

  fun reject(auditorId: AggregateId.Change, reason: String) {
    require(status == PostContentStatus.PRE_AUDIT) { "只有待审核的帖子才能审核拒绝" }
    status = PostContentStatus.AUDIT_FAIL
    raiseEvent(PostContentRejectedEvent(id.toChangeId(), auditorId, reason))
  }

  fun remove() {
    require(status != PostContentStatus.REMOVED) { "帖子已被删除" }
    status = PostContentStatus.REMOVED
    raiseEvent(PostContentRemovedEvent(id.toChangeId()))
  }

  fun canEdit(): Boolean = status == PostContentStatus.PRE_AUDIT || status == PostContentStatus.AUDIT_FAIL
  fun isPublished(): Boolean = status == PostContentStatus.AUDIT_PASS
} 
