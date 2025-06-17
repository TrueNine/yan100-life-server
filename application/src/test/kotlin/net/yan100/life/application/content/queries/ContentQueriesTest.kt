package net.yan100.life.application.content.queries

import net.yan100.compose.Pq
import net.yan100.compose.slf4j
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContentQueriesTest {

  companion object {
    @JvmStatic
    private val log = slf4j<ContentQueriesTest>()
  }

  @Nested
  inner class GetPostDetailQueryTests {

    @Test
    fun `正常 创建GetPostDetailQuery对象`() {
      log.trace("[正常 创建GetPostDetailQuery对象] starting test")
      // given & when
      val query = GetPostDetailQuery(postId = 123L)

      // then
      assertEquals(123L, query.postId)
    }
  }

  @Nested
  inner class GetPostsQueryTests {

    @Test
    fun `正常 创建GetPostsQuery对象使用所有参数`() {
      log.trace("[正常 创建GetPostsQuery对象使用所有参数] starting test")
      // given & when
      val pq = Pq[10, 20] // offset=10, pageSize=20
      val query = GetPostsQuery(
        status = PostContentStatus.AUDIT_PASS,
        type = PostMessageType.JOB,
        pq = pq
      )

      // then
      assertEquals(PostContentStatus.AUDIT_PASS, query.status)
      assertEquals(PostMessageType.JOB, query.type)
      assertEquals(pq, query.pq)
    }

    @Test
    fun `正常 创建GetPostsQuery对象使用默认值`() {
      log.trace("[正常 创建GetPostsQuery对象使用默认值] starting test")
      // given & when
      val query = GetPostsQuery()

      // then
      assertEquals(null, query.status)
      assertEquals(null, query.type)
      assertEquals(Pq.DEFAULT_MAX, query.pq)
    }
  }

  @Nested
  inner class GetUserPostsQueryTests {

    @Test
    fun `正常 创建GetUserPostsQuery对象使用所有参数`() {
      log.trace("[正常 创建GetUserPostsQuery对象使用所有参数] starting test")
      // given & when
      val pq = Pq[30, 15] // offset=30, pageSize=15
      val query = GetUserPostsQuery(
        userId = 456L,
        status = PostContentStatus.PRE_AUDIT,
        pq = pq
      )

      // then
      assertEquals(456L, query.userId)
      assertEquals(PostContentStatus.PRE_AUDIT, query.status)
      assertEquals(pq, query.pq)
    }

    @Test
    fun `正常 创建GetUserPostsQuery对象使用默认值`() {
      log.trace("[正常 创建GetUserPostsQuery对象使用默认值] starting test")
      // given & when
      val query = GetUserPostsQuery(userId = 789L)

      // then
      assertEquals(789L, query.userId)
      assertEquals(null, query.status)
      assertEquals(Pq.DEFAULT_MAX, query.pq)
    }
  }

  @Nested
  inner class GetPendingAuditPostsQueryTests {

    @Test
    fun `正常 创建GetPendingAuditPostsQuery对象使用所有参数`() {
      log.trace("[正常 创建GetPendingAuditPostsQuery对象使用所有参数] starting test")
      // given & when
      val pq = Pq[0, 10] // offset=0, pageSize=10
      val query = GetPendingAuditPostsQuery(pq = pq)

      // then
      assertEquals(pq, query.pq)
    }

    @Test
    fun `正常 创建GetPendingAuditPostsQuery对象使用默认值`() {
      log.trace("[正常 创建GetPendingAuditPostsQuery对象使用默认值] starting test")
      // given & when
      val query = GetPendingAuditPostsQuery()

      // then
      assertEquals(Pq.DEFAULT_MAX, query.pq)
    }
  }

  @Nested
  inner class GetUserMessagesQueryTests {

    @Test
    fun `正常 创建GetUserMessagesQuery对象使用所有参数`() {
      log.trace("[正常 创建GetUserMessagesQuery对象使用所有参数] starting test")
      // given & when
      val pq = Pq[75, 25] // offset=75, pageSize=25
      val query = GetUserMessagesQuery(
        userId = 101L,
        unreadOnly = true,
        pq = pq
      )

      // then
      assertEquals(101L, query.userId)
      assertTrue(query.unreadOnly)
      assertEquals(pq, query.pq)
    }

    @Test
    fun `正常 创建GetUserMessagesQuery对象使用默认值`() {
      log.trace("[正常 创建GetUserMessagesQuery对象使用默认值] starting test")
      // given & when
      val query = GetUserMessagesQuery(userId = 202L)

      // then
      assertEquals(202L, query.userId)
      assertFalse(query.unreadOnly)
      assertEquals(Pq.DEFAULT_MAX, query.pq)
    }
  }

  @Nested
  inner class GetAuditRecordsQueryTests {

    @Test
    fun `正常 创建GetAuditRecordsQuery对象使用所有参数`() {
      log.trace("[正常 创建GetAuditRecordsQuery对象使用所有参数] starting test")
      // given & when
      val pq = Pq[120, 30] // offset=120, pageSize=30
      val query = GetAuditRecordsQuery(
        postId = 303L,
        auditorId = 404L,
        pq = pq
      )

      // then
      assertEquals(303L, query.postId)
      assertEquals(404L, query.auditorId)
      assertEquals(pq, query.pq)
    }

    @Test
    fun `正常 创建GetAuditRecordsQuery对象使用默认值`() {
      log.trace("[正常 创建GetAuditRecordsQuery对象使用默认值] starting test")
      // given & when
      val query = GetAuditRecordsQuery()

      // then
      assertEquals(null, query.postId)
      assertEquals(null, query.auditorId)
      assertEquals(Pq.DEFAULT_MAX, query.pq)
    }
  }
} 