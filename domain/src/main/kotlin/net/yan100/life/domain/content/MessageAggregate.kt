package net.yan100.life.domain.content

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot

/**
 * 消息聚合根
 */
class MessageAggregate(
  override var id: AggregateId,
  val fromUserId: AggregateId,
  val toUserId: AggregateId,
  val postId: AggregateId,
  val content: String,
  var read: Boolean = false,
) : AggregateRoot(id) {

  companion object {
    fun create(
      fromUserId: AggregateId,
      toUserId: AggregateId,
      postId: AggregateId,
      content: String,
    ): MessageAggregate {
      require(fromUserId != toUserId) { "不能给自己发送消息" }
      require(content.isNotBlank()) { "消息内容不能为空" }

      val messageId = AggregateId.Create.Default()
      val message = MessageAggregate(
        id = messageId,
        fromUserId = fromUserId,
        toUserId = toUserId,
        postId = postId,
        content = content,
        read = false
      )

      message.raiseEvent(
        MessageSentEvent(
          aggregateId = messageId,
          fromUserId = fromUserId,
          toUserId = toUserId,
          postId = postId,
          content = content
        )
      )

      return message
    }
  }

  fun markAsRead() {
    if (read) return
    read = true
    raiseEvent(MessageReadEvent(id.toChangeId()))
  }

  fun isRead(): Boolean = read
} 
