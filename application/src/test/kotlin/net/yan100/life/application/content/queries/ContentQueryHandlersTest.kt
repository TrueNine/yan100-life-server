package net.yan100.life.application.content.queries

import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import net.yan100.compose.Pq
import net.yan100.compose.slf4j
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ContentQueryHandlersTest {

  companion object {
    @JvmStatic
    private val log = slf4j<ContentQueryHandlersTest>()
  }

  private lateinit var getPostDetailQueryHandler: GetPostDetailQueryHandler
  private lateinit var getPostsQueryHandler: GetPostsQueryHandler
  private lateinit var getUserPostsQueryHandler: GetUserPostsQueryHandler
  private lateinit var getPendingAuditPostsQueryHandler: GetPendingAuditPostsQueryHandler
  private lateinit var getUserMessagesQueryHandler: GetUserMessagesQueryHandler
  private lateinit var getAuditRecordsQueryHandler: GetAuditRecordsQueryHandler

  @BeforeEach
  fun setUp() {
    log.trace("[setUp] initializing query handlers")
    getPostDetailQueryHandler = GetPostDetailQueryHandler()
    getPostsQueryHandler = GetPostsQueryHandler()
    getUserPostsQueryHandler = GetUserPostsQueryHandler()
    getPendingAuditPostsQueryHandler = GetPendingAuditPostsQueryHandler()
    getUserMessagesQueryHandler = GetUserMessagesQueryHandler()
    getAuditRecordsQueryHandler = GetAuditRecordsQueryHandler()
  }

  @AfterEach
  fun tearDown() {
    log.trace("[tearDown] clearing all mocks")
    clearAllMocks()
  }

  @Nested
  inner class GetPostDetailTests {

    @Test
    fun `正常 处理获取帖子详情查询`() = runTest {
      log.trace("[正常 处理获取帖子详情查询] starting test")
      // given
      val query = GetPostDetailQuery(postId = 1L)

      // when
      val result = getPostDetailQueryHandler.handle(query)

      // then
      // 由于是TODO实现，目前返回null
      assertNull(result)
    }
  }

  @Nested
  inner class GetPostsTests {

    @Test
    fun `正常 处理获取帖子列表查询`() = runTest {
      log.trace("[正常 处理获取帖子列表查询] starting test")
      // given
      val query = GetPostsQuery(
        status = PostContentStatus.AUDIT_PASS,
        type = PostMessageType.JOB,
        pq = Pq.DEFAULT_MAX
      )

      // when
      val result = getPostsQueryHandler.handle(query)

      // then
      assertNotNull(result)
      // TODO: 等待Pr类型API稳定后，再验证具体内容
    }
  }

  @Nested
  inner class GetUserPostsTests {

    @Test
    fun `正常 处理获取用户帖子查询`() = runTest {
      log.trace("[正常 处理获取用户帖子查询] starting test")
      // given
      val query = GetUserPostsQuery(
        userId = 1L,
        status = PostContentStatus.AUDIT_PASS,
        pq = Pq.DEFAULT_MAX
      )

      // when
      val result = getUserPostsQueryHandler.handle(query)

      // then
      assertNotNull(result)
      // TODO: 等待Pr类型API稳定后，再验证具体内容
    }
  }

  @Nested
  inner class GetPendingAuditPostsTests {

    @Test
    fun `正常 处理获取待审核帖子查询`() = runTest {
      log.trace("[正常 处理获取待审核帖子查询] starting test")
      // given
      val query = GetPendingAuditPostsQuery(pq = Pq.DEFAULT_MAX)

      // when
      val result = getPendingAuditPostsQueryHandler.handle(query)

      // then
      assertNotNull(result)
      // TODO: 等待Pr类型API稳定后，再验证具体内容
    }
  }

  @Nested
  inner class GetUserMessagesTests {

    @Test
    fun `正常 处理获取用户消息查询`() = runTest {
      log.trace("[正常 处理获取用户消息查询] starting test")
      // given
      val query = GetUserMessagesQuery(
        userId = 1L,
        unreadOnly = false,
        pq = Pq.DEFAULT_MAX
      )

      // when
      val result = getUserMessagesQueryHandler.handle(query)

      // then
      assertNotNull(result)
      // TODO: 等待Pr类型API稳定后，再验证具体内容
    }
  }

  @Nested
  inner class GetAuditRecordsTests {

    @Test
    fun `正常 处理获取审核记录查询`() = runTest {
      log.trace("[正常 处理获取审核记录查询] starting test")
      // given
      val query = GetAuditRecordsQuery(
        postId = 1L,
        auditorId = 2L,
        pq = Pq.DEFAULT_MAX
      )

      // when
      val result = getAuditRecordsQueryHandler.handle(query)

      // then
      assertNotNull(result)
      // TODO: 等待Pr类型API稳定后，再验证具体内容
    }
  }
} 