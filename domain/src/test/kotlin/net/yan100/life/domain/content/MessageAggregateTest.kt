package net.yan100.life.domain.content

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MessageAggregateTest {

  @Nested
  inner class CreateTests {

    @Test
    fun `正常 创建消息成功`() {
      // given
      val fromUserId = AggregateId.queryByStringId("1001")
      val toUserId = AggregateId.queryByStringId("1002")
      val postId = AggregateId.queryByStringId("2001")
      val content = "这是一条测试消息"

      // when
      val message = MessageAggregate.create(
        fromUserId = fromUserId,
        toUserId = toUserId,
        postId = postId,
        content = content
      )

      // then
      assertEquals(fromUserId, message.fromUserId)
      assertEquals(toUserId, message.toUserId)
      assertEquals(postId, message.postId)
      assertEquals(content, message.content)
      assertFalse(message.read)
      assertEquals(1, message.domainEvents.size)
      assertTrue(message.domainEvents.first() is MessageSentEvent)
    }

    @Test
    fun `异常 给自己发送消息时抛出异常`() {
      // given
      val userId = AggregateId.queryByStringId("1001")
      val postId = AggregateId.queryByStringId("2001")
      val content = "测试消息"

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        MessageAggregate.create(
          fromUserId = userId,
          toUserId = userId,
          postId = postId,
          content = content
        )
      }
      assertEquals("不能给自己发送消息", exception.message)
    }

    @Test
    fun `异常 消息内容为空时抛出异常`() {
      // given
      val fromUserId = AggregateId.queryByStringId("1001")
      val toUserId = AggregateId.queryByStringId("1002")
      val postId = AggregateId.queryByStringId("2001")
      val content = ""

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        MessageAggregate.create(
          fromUserId = fromUserId,
          toUserId = toUserId,
          postId = postId,
          content = content
        )
      }
      assertEquals("消息内容不能为空", exception.message)
    }

    @Test
    fun `异常 消息内容为空白字符时抛出异常`() {
      // given
      val fromUserId = AggregateId.queryByStringId("1001")
      val toUserId = AggregateId.queryByStringId("1002")
      val postId = AggregateId.queryByStringId("2001")
      val content = "   "

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        MessageAggregate.create(
          fromUserId = fromUserId,
          toUserId = toUserId,
          postId = postId,
          content = content
        )
      }
      assertEquals("消息内容不能为空", exception.message)
    }
  }

  @Nested
  inner class MarkAsReadTests {

    @Test
    fun `正常 标记消息为已读成功`() {
      // given
      val message = MessageAggregate.create(
        fromUserId = AggregateId.queryByStringId("1001"),
        toUserId = AggregateId.queryByStringId("1002"),
        postId = AggregateId.queryByStringId("2001"),
        content = "测试消息"
      )
      message.clearEvents() // 清除创建事件
      
      // 激活聚合根，将 Create ID 转换为 Change ID
      message.active(AggregateId.change("3001"))

      // when
      message.markAsRead()

      // then
      assertTrue(message.read)
      assertTrue(message.isRead())
      assertEquals(1, message.domainEvents.size)
      assertTrue(message.domainEvents.first() is MessageReadEvent)
    }

    @Test
    fun `正常 重复标记已读消息不产生新事件`() {
      // given
      val message = MessageAggregate.create(
        fromUserId = AggregateId.queryByStringId("1001"),
        toUserId = AggregateId.queryByStringId("1002"),
        postId = AggregateId.queryByStringId("2001"),
        content = "测试消息"
      )
      message.clearEvents()
      
      // 激活聚合根，将 Create ID 转换为 Change ID
      message.active(AggregateId.change("3002"))
      
      message.markAsRead()
      message.clearEvents()

      // when
      message.markAsRead()

      // then
      assertTrue(message.read)
      assertTrue(message.isRead())
      assertEquals(0, message.domainEvents.size) // 不应该产生新事件
    }
  }
} 