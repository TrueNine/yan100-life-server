package net.yan100.life.application.user.queries

import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runTest
import net.yan100.compose.Pq
import net.yan100.compose.slf4j
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserQueryHandlersTest {

  companion object {
    @JvmStatic
    private val log = slf4j<UserQueryHandlersTest>()
  }

  private lateinit var getUserDetailQueryHandler: GetUserDetailQueryHandler
  private lateinit var getUserByAccountQueryHandler: GetUserByAccountQueryHandler
  private lateinit var getUserByPhoneQueryHandler: GetUserByPhoneQueryHandler
  private lateinit var searchUsersQueryHandler: SearchUsersQueryHandler

  @BeforeEach
  fun setUp() {
    log.trace("[setUp] initializing query handlers")
    getUserDetailQueryHandler = GetUserDetailQueryHandler()
    getUserByAccountQueryHandler = GetUserByAccountQueryHandler()
    getUserByPhoneQueryHandler = GetUserByPhoneQueryHandler()
    searchUsersQueryHandler = SearchUsersQueryHandler()
  }

  @AfterEach
  fun tearDown() {
    log.trace("[tearDown] clearing all mocks")
    clearAllMocks()
  }

  @Nested
  inner class GetUserDetailTests {

    @Test
    fun `正常 处理获取用户详情查询`() = runTest {
      log.trace("[正常 处理获取用户详情查询] starting test")
      // given
      val query = GetUserDetailQuery(userId = 1L)

      // when
      val result = getUserDetailQueryHandler.handle(query)

      // then
      // 由于是TODO实现，目前返回null
      assertNull(result)
    }
  }

  @Nested
  inner class GetUserByAccountTests {

    @Test
    fun `正常 处理通过账号查询用户`() = runTest {
      log.trace("[正常 处理通过账号查询用户] starting test")
      // given
      val query = GetUserByAccountQuery(account = "testuser")

      // when
      val result = getUserByAccountQueryHandler.handle(query)

      // then
      // 由于是TODO实现，目前返回null
      assertNull(result)
    }
  }

  @Nested
  inner class GetUserByPhoneTests {

    @Test
    fun `正常 处理通过手机号查询用户`() = runTest {
      log.trace("[正常 处理通过手机号查询用户] starting test")
      // given
      val query = GetUserByPhoneQuery(phone = "1234567890")

      // when
      val result = getUserByPhoneQueryHandler.handle(query)

      // then
      // 由于是TODO实现，目前返回null
      assertNull(result)
    }
  }

  @Nested
  inner class SearchUsersTests {

    @Test
    fun `正常 处理搜索用户查询`() = runTest {
      log.trace("[正常 处理搜索用户查询] starting test")
      // given
      val query = SearchUsersQuery(
        keyword = "test",
        pq = Pq.DEFAULT_MAX
      )

      // when
      val result = searchUsersQueryHandler.handle(query)

      // then
      assertNotNull(result)
      // TODO: 等待Pr类型API稳定后，再验证具体内容
    }
  }
} 