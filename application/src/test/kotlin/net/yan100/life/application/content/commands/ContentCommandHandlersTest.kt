package net.yan100.life.application.content.commands

import io.mockk.*
import kotlinx.coroutines.test.runTest
import net.yan100.compose.slf4j
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEventPublisher
import net.yan100.life.domain.content.PostContentAggregate
import net.yan100.life.domain.content.PostContentRepository
import net.yan100.life.domain.content.MessageAggregate
import net.yan100.life.domain.content.MessageRepository
import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ContentCommandHandlersTest {

  companion object {
    @JvmStatic
    private val log = slf4j<ContentCommandHandlersTest>()
  }

  private lateinit var postRepository: PostContentRepository
  private lateinit var messageRepository: MessageRepository
  private lateinit var eventPublisher: DomainEventPublisher

  private lateinit var createPostCommandHandler: CreatePostCommandHandler
  private lateinit var updatePostCommandHandler: UpdatePostCommandHandler
  private lateinit var submitPostForAuditCommandHandler: SubmitPostForAuditCommandHandler
  private lateinit var approvePostCommandHandler: ApprovePostCommandHandler
  private lateinit var rejectPostCommandHandler: RejectPostCommandHandler
  private lateinit var removePostCommandHandler: RemovePostCommandHandler
  private lateinit var sendMessageCommandHandler: SendMessageCommandHandler

  @BeforeEach
  fun setUp() {
    log.trace("[setUp] initializing mocks and handlers")
    postRepository = mockk()
    messageRepository = mockk()
    eventPublisher = mockk()
    createPostCommandHandler = CreatePostCommandHandler(postRepository, eventPublisher)
    updatePostCommandHandler = UpdatePostCommandHandler(postRepository, eventPublisher)
    submitPostForAuditCommandHandler = SubmitPostForAuditCommandHandler(postRepository, eventPublisher)
    approvePostCommandHandler = ApprovePostCommandHandler(postRepository, eventPublisher)
    rejectPostCommandHandler = RejectPostCommandHandler(postRepository, eventPublisher)
    removePostCommandHandler = RemovePostCommandHandler(postRepository, eventPublisher)
    sendMessageCommandHandler = SendMessageCommandHandler(messageRepository, postRepository, eventPublisher)
  }

  @AfterEach
  fun tearDown() {
    log.trace("[tearDown] clearing all mocks")
    clearAllMocks()
  }

  @Nested
  inner class CreatePostTests {

    @Test
    fun `正常 创建帖子成功`() = runTest {
      log.trace("[正常 创建帖子成功] starting test")
      // given
      val command = CreatePostCommand(
        title = "Test Title",
        content = "Test Content", 
        type = PostMessageType.JOB,
        pubUserAccountId = AggregateId.change("123")
      )
      val mockAggregateId = mockk<AggregateId>(relaxed = true)
      val mockResultId = mockk<AggregateId.Result>(relaxed = true)
      every { mockAggregateId.toResultId() } returns mockResultId

      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { id } returns mockAggregateId
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.save(any()) } returns mockPost
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      val result = createPostCommandHandler.handle(command)

      // then
      coVerify(exactly = 1) { postRepository.save(any()) }
      verify(exactly = 1) { mockPost.domainEvents }
      verify(exactly = 1) { eventPublisher.publishAll(any()) }
      verify(exactly = 1) { mockPost.clearEvents() }
      verify(exactly = 1) { mockPost.id }
      verify(exactly = 1) { mockAggregateId.toResultId() }
      assertEquals(mockResultId, result)

      confirmVerified(postRepository, eventPublisher, mockPost, mockAggregateId)
    }
  }

  @Nested
  inner class UpdatePostTests {

    @Test
    fun `正常 更新帖子成功`() = runTest {
      log.trace("[正常 更新帖子成功] starting test")
      // given
      val userId = AggregateId.change("123")
      val postId = AggregateId.change("456")
      val command = UpdatePostCommand(
        postId = postId,
        title = "Updated Title",
        content = "Updated Content",
        userId = userId
      )
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { pubUserAccountId } returns userId
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost
      coEvery { postRepository.save(mockPost) } returns mockPost
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      updatePostCommandHandler.handle(command)

      // then
      coVerify { postRepository.findById(postId.toQueryId()) }
      verify { mockPost.pubUserAccountId }
      verify { mockPost.update("Updated Title", "Updated Content") }
      coVerify { postRepository.save(mockPost) }
      verify { mockPost.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockPost.clearEvents() }

      confirmVerified(postRepository, eventPublisher, mockPost)
    }

    @Test
    fun `异常 帖子不存在时抛出异常`() = runTest {
      log.trace("[异常 帖子不存在时抛出异常] starting test")
      // given
      val command = UpdatePostCommand(
        postId = AggregateId.change("789"),
        userId = AggregateId.change("101")
      )
      coEvery { postRepository.findById(any()) } returns null

      // when & then
      val exception = assertFailsWith<IllegalStateException> {
        updatePostCommandHandler.handle(command)
      }
      assertEquals("帖子不存在: ${command.postId}", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }

    @Test
    fun `异常 用户无权修改时抛出异常`() = runTest {
      log.trace("[异常 用户无权修改时抛出异常] starting test")
      // given
      val ownerId = AggregateId.change("111")
      val attackerId = AggregateId.change("222")
      val postId = AggregateId.change("333")

      val command = UpdatePostCommand(
        postId = postId,
        title = "Updated Title",
        content = "Updated Content",
        userId = attackerId
      )
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { pubUserAccountId } returns ownerId
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        updatePostCommandHandler.handle(command)
      }
      assertEquals("无权修改此帖子", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }
  }

  @Nested
  inner class SubmitPostForAuditTests {

    @Test
    fun `正常 提交审核成功`() = runTest {
      log.trace("[正常 提交审核成功] starting test")
      // given
      val userId = AggregateId.change("444")
      val postId = AggregateId.change("555")
      val command = SubmitPostForAuditCommand(postId = postId, userId = userId)
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { pubUserAccountId } returns userId
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost
      coEvery { postRepository.save(mockPost) } returns mockPost
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      submitPostForAuditCommandHandler.handle(command)

      // then
      coVerify { postRepository.findById(postId.toQueryId()) }
      verify { mockPost.pubUserAccountId }
      verify { mockPost.submitForAudit() }
      coVerify { postRepository.save(mockPost) }
      verify { mockPost.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockPost.clearEvents() }

      confirmVerified(postRepository, eventPublisher, mockPost)
    }

    @Test
    fun `异常 帖子不存在时抛出异常`() = runTest {
      log.trace("[异常 帖子不存在时抛出异常] starting test")
      // given
      val command = SubmitPostForAuditCommand(
        postId = AggregateId.change("666"),
        userId = AggregateId.change("777")
      )
      coEvery { postRepository.findById(any()) } returns null

      // when & then
      val exception = assertFailsWith<IllegalStateException> {
        submitPostForAuditCommandHandler.handle(command)
      }
      assertEquals("帖子不存在: ${command.postId}", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }

    @Test
    fun `异常 用户无权操作时抛出异常`() = runTest {
      log.trace("[异常 用户无权操作时抛出异常] starting test")
      // given
      val ownerId = AggregateId.change("888")
      val attackerId = AggregateId.change("999")
      val postId = AggregateId.change("1001")

      val command = SubmitPostForAuditCommand(postId = postId, userId = attackerId)
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { pubUserAccountId } returns ownerId
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        submitPostForAuditCommandHandler.handle(command)
      }
      assertEquals("无权操作此帖子", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }
  }

  @Nested
  inner class ApprovePostTests {

    @Test
    fun `正常 审核通过帖子成功`() = runTest {
      log.trace("[正常 审核通过帖子成功] starting test")
      // given
      val postId = AggregateId.change("1111")
      val auditorId = AggregateId.change("2222")
      val command = ApprovePostCommand(postId = postId, auditorId = auditorId)
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost
      coEvery { postRepository.save(mockPost) } returns mockPost
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      approvePostCommandHandler.handle(command)

      // then
      coVerify { postRepository.findById(postId.toQueryId()) }
      verify { mockPost.approve(any()) }
      coVerify { postRepository.save(mockPost) }
      verify { mockPost.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockPost.clearEvents() }

      confirmVerified(postRepository, eventPublisher, mockPost)
    }

    @Test
    fun `异常 帖子不存在时抛出异常`() = runTest {
      log.trace("[异常 帖子不存在时抛出异常] starting test")
      // given
      val command = ApprovePostCommand(
        postId = AggregateId.change("3333"),
        auditorId = AggregateId.change("4444")
      )
      coEvery { postRepository.findById(any()) } returns null

      // when & then
      val exception = assertFailsWith<IllegalStateException> {
        approvePostCommandHandler.handle(command)
      }
      assertEquals("帖子不存在: ${command.postId}", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }
  }

  @Nested
  inner class RejectPostTests {

    @Test
    fun `正常 拒绝帖子成功`() = runTest {
      log.trace("[正常 拒绝帖子成功] starting test")
      // given
      val postId = AggregateId.change("5555")
      val auditorId = AggregateId.change("6666")
      val reason = "内容不合规"
      val command = RejectPostCommand(postId = postId, auditorId = auditorId, reason = reason)
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost
      coEvery { postRepository.save(mockPost) } returns mockPost
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      rejectPostCommandHandler.handle(command)

      // then
      coVerify { postRepository.findById(postId.toQueryId()) }
      verify { mockPost.reject(auditorId, reason) }
      coVerify { postRepository.save(mockPost) }
      verify { mockPost.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockPost.clearEvents() }

      confirmVerified(postRepository, eventPublisher, mockPost)
    }

    @Test
    fun `异常 帖子不存在时抛出异常`() = runTest {
      log.trace("[异常 帖子不存在时抛出异常] starting test")
      // given
      val command = RejectPostCommand(
        postId = AggregateId.change("7777"),
        auditorId = AggregateId.change("8888"),
        reason = "test reason"
      )
      coEvery { postRepository.findById(any()) } returns null

      // when & then
      val exception = assertFailsWith<IllegalStateException> {
        rejectPostCommandHandler.handle(command)
      }
      assertEquals("帖子不存在: ${command.postId}", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }
  }

  @Nested
  inner class RemovePostTests {

    @Test
    fun `正常 删除帖子成功`() = runTest {
      log.trace("[正常 删除帖子成功] starting test")
      // given
      val userId = AggregateId.change("9999")
      val postId = AggregateId.change("1010")
      val command = RemovePostCommand(postId = postId, userId = userId)
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { pubUserAccountId } returns userId
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost
      coEvery { postRepository.save(mockPost) } returns mockPost
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      removePostCommandHandler.handle(command)

      // then
      coVerify { postRepository.findById(postId.toQueryId()) }
      verify { mockPost.pubUserAccountId }
      verify { mockPost.remove() }
      coVerify { postRepository.save(mockPost) }
      verify { mockPost.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockPost.clearEvents() }

      confirmVerified(postRepository, eventPublisher, mockPost)
    }

    @Test
    fun `异常 帖子不存在时抛出异常`() = runTest {
      log.trace("[异常 帖子不存在时抛出异常] starting test")
      // given
      val command = RemovePostCommand(
        postId = AggregateId.change("1212"),
        userId = AggregateId.change("1313")
      )
      coEvery { postRepository.findById(any()) } returns null

      // when & then
      val exception = assertFailsWith<IllegalStateException> {
        removePostCommandHandler.handle(command)
      }
      assertEquals("帖子不存在: ${command.postId}", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }

    @Test
    fun `异常 用户无权删除时抛出异常`() = runTest {
      log.trace("[异常 用户无权删除时抛出异常] starting test")
      // given
      val ownerId = AggregateId.change("1414")
      val attackerId = AggregateId.change("1515")
      val postId = AggregateId.change("1616")
      val command = RemovePostCommand(postId = postId, userId = attackerId)
      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { pubUserAccountId } returns ownerId
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        removePostCommandHandler.handle(command)
      }
      assertEquals("无权删除此帖子", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }
  }

  @Nested
  inner class SendMessageTests {

    @Test
    fun `正常 发送消息成功`() = runTest {
      log.trace("[正常 发送消息成功] starting test")
      // given
      val fromUserId = AggregateId.queryByStringId("1001")
      val toUserId = AggregateId.queryByStringId("1002")
      val postId = AggregateId.queryByStringId("2001")
      val content = "这是一条测试消息"
      val command = SendMessageCommand(
        fromUserId = fromUserId,
        toUserId = toUserId,
        postId = postId,
        content = content
      )

      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { isPublished() } returns true
      }

      val mockMessageId = mockk<AggregateId>(relaxed = true)
      val mockResultId = mockk<AggregateId.Result>(relaxed = true)
      every { mockMessageId.toResultId() } returns mockResultId

      val mockMessage = mockk<MessageAggregate>(relaxed = true) {
        every { id } returns mockMessageId
        every { domainEvents } returns emptyList()
      }

      coEvery { postRepository.findById(postId.toQueryId()) } returns mockPost
      coEvery { messageRepository.save(any()) } returns mockMessage
      every { eventPublisher.publishAll(any()) } returns Unit

      // when
      val result = sendMessageCommandHandler.handle(command)

      // then
      coVerify(exactly = 1) { postRepository.findById(postId.toQueryId()) }
      verify(exactly = 1) { mockPost.isPublished() }
      coVerify(exactly = 1) { messageRepository.save(any()) }
      verify(exactly = 1) { mockMessage.domainEvents }
      verify(exactly = 1) { eventPublisher.publishAll(any()) }
      verify(exactly = 1) { mockMessage.clearEvents() }
      verify(exactly = 1) { mockMessage.id }
      verify(exactly = 1) { mockMessageId.toResultId() }
      assertEquals(mockResultId, result)

      confirmVerified(postRepository, messageRepository, eventPublisher, mockPost, mockMessage, mockMessageId)
    }

    @Test
    fun `异常 帖子不存在时抛出异常`() = runTest {
      log.trace("[异常 帖子不存在时抛出异常] starting test")
      // given
      val command = SendMessageCommand(
        fromUserId = AggregateId.queryByStringId("2001"),
        toUserId = AggregateId.queryByStringId("2002"),
        postId = AggregateId.queryByStringId("3001"),
        content = "测试消息"
      )
      coEvery { postRepository.findById(any()) } returns null

      // when & then
      val exception = assertFailsWith<IllegalStateException> {
        sendMessageCommandHandler.handle(command)
      }
      assertEquals("帖子不存在: ${command.postId}", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      confirmVerified(postRepository)
    }

    @Test
    fun `异常 帖子未发布时抛出异常`() = runTest {
      log.trace("[异常 帖子未发布时抛出异常] starting test")
      // given
      val command = SendMessageCommand(
        fromUserId = AggregateId.queryByStringId("3001"),
        toUserId = AggregateId.queryByStringId("3002"),
        postId = AggregateId.queryByStringId("4001"),
        content = "测试消息"
      )

      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { isPublished() } returns false
      }

      coEvery { postRepository.findById(command.postId.toQueryId()) } returns mockPost

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        sendMessageCommandHandler.handle(command)
      }
      assertEquals("只能对已发布的帖子发送消息", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      verify(exactly = 1) { mockPost.isPublished() }
      confirmVerified(postRepository, mockPost)
    }

    @Test
    fun `异常 给自己发送消息时抛出异常`() = runTest {
      log.trace("[异常 给自己发送消息时抛出异常] starting test")
      // given
      val userId = AggregateId.queryByStringId("4001")
      val command = SendMessageCommand(
        fromUserId = userId,
        toUserId = userId,
        postId = AggregateId.queryByStringId("5001"),
        content = "测试消息"
      )

      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { isPublished() } returns true
      }

      coEvery { postRepository.findById(command.postId.toQueryId()) } returns mockPost

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        sendMessageCommandHandler.handle(command)
      }
      assertEquals("不能给自己发送消息", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      verify(exactly = 1) { mockPost.isPublished() }
      confirmVerified(postRepository, mockPost)
    }

    @Test
    fun `异常 消息内容为空时抛出异常`() = runTest {
      log.trace("[异常 消息内容为空时抛出异常] starting test")
      // given
      val command = SendMessageCommand(
        fromUserId = AggregateId.queryByStringId("5001"),
        toUserId = AggregateId.queryByStringId("5002"),
        postId = AggregateId.queryByStringId("6001"),
        content = ""
      )

      val mockPost = mockk<PostContentAggregate>(relaxed = true) {
        every { isPublished() } returns true
      }

      coEvery { postRepository.findById(command.postId.toQueryId()) } returns mockPost

      // when & then
      val exception = assertFailsWith<IllegalArgumentException> {
        sendMessageCommandHandler.handle(command)
      }
      assertEquals("消息内容不能为空", exception.message)
      coVerify(exactly = 1) { postRepository.findById(command.postId.toQueryId()) }
      verify(exactly = 1) { mockPost.isPublished() }
      confirmVerified(postRepository, mockPost)
    }
  }
} 
