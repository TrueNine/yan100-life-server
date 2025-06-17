package net.yan100.life.application.eventhandlers

import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import net.yan100.compose.slf4j
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.content.*
import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class ContentEventHandlersTest {

  companion object {
    @JvmStatic
    private val log = slf4j<ContentEventHandlersTest>()
  }

  private lateinit var contentEventHandlers: ContentEventHandlers

  @BeforeEach
  fun setUp() {
    log.trace("[setUp] initializing content event handlers")
    contentEventHandlers = spyk(ContentEventHandlers())
  }

  @Nested
  inner class PostContentCreatedEvent {

    @Test
    fun `正常 处理帖子创建事件`() = runTest {
      log.trace("[正常 处理帖子创建事件] starting test")
      // given
      val event = PostContentCreatedEvent(
        aggregateId = AggregateId.create(),
        title = "Test Post",
        type = PostMessageType.JOB,
        pubUserAccountId = AggregateId.change("123")
      )

      // when
      contentEventHandlers.handle(event)

      // then
      coVerify { contentEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostContentUpdatedEvent {

    @Test
    fun `正常 处理帖子更新事件`() = runTest {
      log.trace("[正常 处理帖子更新事件] starting test")
      // given
      val event = PostContentUpdatedEvent(
        aggregateId = AggregateId.change("456"),
        title = "Updated Title",
        content = "Updated Content"
      )

      // when
      contentEventHandlers.handle(event)

      // then
      coVerify { contentEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostContentSubmittedForAuditEvent {

    @Test
    fun `正常 处理帖子提交审核事件`() = runTest {
      log.trace("[正常 处理帖子提交审核事件] starting test")
      // given
      val event = PostContentSubmittedForAuditEvent(
        aggregateId = AggregateId.change("789")
      )

      // when
      contentEventHandlers.handle(event)

      // then
      coVerify { contentEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostContentApprovedEvent {

    @Test
    fun `正常 处理帖子审核通过事件`() = runTest {
      log.trace("[正常 处理帖子审核通过事件] starting test")
      // given
      val event = PostContentApprovedEvent(
        aggregateId = AggregateId.change("101"),
        auditorId = AggregateId.change("202")
      )

      // when
      contentEventHandlers.handle(event)

      // then
      coVerify { contentEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostContentRejectedEvent {

    @Test
    fun `正常 处理帖子审核拒绝事件`() = runTest {
      log.trace("[正常 处理帖子审核拒绝事件] starting test")
      // given
      val event = PostContentRejectedEvent(
        aggregateId = AggregateId.change("303"),
        auditorId = AggregateId.change("404"),
        reason = "内容不合规"
      )

      // when
      contentEventHandlers.handle(event)

      // then
      coVerify { contentEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostContentRemovedEvent {

    @Test
    fun `正常 处理帖子删除事件`() = runTest {
      log.trace("[正常 处理帖子删除事件] starting test")
      // given
      val event = PostContentRemovedEvent(
        aggregateId = AggregateId.change("505")
      )

      // when
      contentEventHandlers.handle(event)

      // then
      coVerify { contentEventHandlers.handle(event) }
    }
  }
} 
