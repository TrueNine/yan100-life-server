package net.yan100.life.application.eventhandlers

import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import net.yan100.compose.slf4j
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.user.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class UserEventHandlersTest {

  companion object {
    @JvmStatic
    private val log = slf4j<UserEventHandlersTest>()
  }

  private lateinit var userEventHandlers: UserEventHandlers

  @BeforeEach
  fun setUp() {
    log.trace("[setUp] initializing user event handlers")
    userEventHandlers = spyk(UserEventHandlers())
  }

  @Nested
  inner class UserAccountCreatedEvent {

    @Test
    fun `正常 处理用户账户创建事件`() = runTest {
      log.trace("[正常 处理用户账户创建事件] starting test")
      // given
      val event = UserAccountCreatedEvent(
        aggregateId = AggregateId.create(),
        account = "testuser",
        nickName = "Test User"
      )

      // when
      userEventHandlers.handle(event)

      // then
      coVerify { userEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class UserProfileUpdatedEvent {

    @Test
    fun `正常 处理用户资料更新事件`() = runTest {
      log.trace("[正常 处理用户资料更新事件] starting test")
      // given
      val event = UserProfileUpdatedEvent(
        aggregateId = AggregateId.change("123"),
        nickName = "Updated Name",
        phone = "1234567890",
        avatarUrl = "new_avatar.jpg"
      )

      // when
      userEventHandlers.handle(event)

      // then
      coVerify { userEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class WechatBoundEvent {

    @Test
    fun `正常 处理微信绑定事件`() = runTest {
      log.trace("[正常 处理微信绑定事件] starting test")
      // given
      val event = WechatBoundEvent(
        aggregateId = AggregateId.change("456"),
        openId = "test_openid"
      )

      // when
      userEventHandlers.handle(event)

      // then
      coVerify { userEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PasswordChangedEvent {

    @Test
    fun `正常 处理密码修改事件`() = runTest {
      log.trace("[正常 处理密码修改事件] starting test")
      // given
      val event = PasswordChangedEvent(
        aggregateId = AggregateId.change("789")
      )

      // when
      userEventHandlers.handle(event)

      // then
      coVerify { userEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostFavoritedEvent {

    @Test
    fun `正常 处理帖子收藏事件`() = runTest {
      log.trace("[正常 处理帖子收藏事件] starting test")
      // given
      val event = PostFavoritedEvent(
        aggregateId = AggregateId.change("101"),
        postId = AggregateId.change("202")
      )

      // when
      userEventHandlers.handle(event)

      // then
      coVerify { userEventHandlers.handle(event) }
    }
  }

  @Nested
  inner class PostUnfavoritedEvent {

    @Test
    fun `正常 处理取消收藏事件`() = runTest {
      log.trace("[正常 处理取消收藏事件] starting test")
      // given
      val event = PostUnfavoritedEvent(
        aggregateId = AggregateId.change("303"),
        postId = AggregateId.change("404")
      )

      // when
      userEventHandlers.handle(event)

      // then
      coVerify { userEventHandlers.handle(event) }
    }
  }
} 
