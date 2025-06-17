package net.yan100.life.application.user.queries

import net.yan100.compose.Pq
import net.yan100.compose.slf4j
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserQueriesTest {

  companion object {
    @JvmStatic
    private val log = slf4j<UserQueriesTest>()
  }

  @Nested
  inner class GetUserDetailQueryTests {

    @Test
    fun `正常 创建GetUserDetailQuery对象`() {
      log.trace("[正常 创建GetUserDetailQuery对象] starting test")
      // given & when
      val query = GetUserDetailQuery(userId = 123L)

      // then
      assertEquals(123L, query.userId)
    }
  }

  @Nested
  inner class GetUserByAccountQueryTests {

    @Test
    fun `正常 创建GetUserByAccountQuery对象`() {
      log.trace("[正常 创建GetUserByAccountQuery对象] starting test")
      // given & when
      val query = GetUserByAccountQuery(account = "testuser")

      // then
      assertEquals("testuser", query.account)
    }
  }

  @Nested
  inner class GetUserByPhoneQueryTests {

    @Test
    fun `正常 创建GetUserByPhoneQuery对象`() {
      log.trace("[正常 创建GetUserByPhoneQuery对象] starting test")
      // given & when
      val query = GetUserByPhoneQuery(phone = "1234567890")

      // then
      assertEquals("1234567890", query.phone)
    }
  }

  @Nested
  inner class SearchUsersQueryTests {

    @Test
    fun `正常 创建SearchUsersQuery对象使用所有参数`() {
      log.trace("[正常 创建SearchUsersQuery对象使用所有参数] starting test")
      // given & when
      val pq = Pq[20, 20] // offset=20, pageSize=20
      val query = SearchUsersQuery(
        keyword = "test",
        pq = pq
      )

      // then
      assertEquals("test", query.keyword)
      assertEquals(pq, query.pq)
    }

    @Test
    fun `正常 创建SearchUsersQuery对象使用默认值`() {
      log.trace("[正常 创建SearchUsersQuery对象使用默认值] starting test")
      // given & when
      val query = SearchUsersQuery()

      // then
      assertEquals(null, query.keyword)
      assertEquals(Pq.DEFAULT_MAX, query.pq)
    }
  }
} 