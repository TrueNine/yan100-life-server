package net.yan100.life.domain.user

import io.mockk.mockk
import io.mockk.verify
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEventPublisher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.junit.jupiter.api.Nested

class UserAccountAggregateTest {
  private lateinit var eventPublisher: DomainEventPublisher

  @BeforeEach
  fun setup() {
    eventPublisher = mockk(relaxed = true)
  }

  @Nested
  inner class Create {
    @Test
    fun `正常创建用户`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick",
        phone = "13333333333"
      )
      assertEquals("acc", agg.account)
      assertEquals("pwd", agg.passwordEnc)
      assertEquals("nick", agg.nickName)
      assertEquals("13333333333", agg.phone)
    }
  }

  @Nested
  inner class UpdateProfile {
    @Test
    fun `正常更新资料`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      agg.updateProfile(nickName = "newNick", phone = "18888888888", avatarUrl = "url")
      assertEquals("newNick", agg.nickName)
      assertEquals("18888888888", agg.phone)
      assertEquals("url", agg.avatarUrl)
      val event = agg.domainEvents.last()
      assertTrue(event is UserProfileUpdatedEvent)
      val e = event as UserProfileUpdatedEvent
      assertEquals("newNick", e.nickName)
      assertEquals("18888888888", e.phone)
      assertEquals("url", e.avatarUrl)
    }
  }

  @Nested
  inner class BindWechat {
    @Test
    fun `首次绑定微信成功`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      agg.bindWechat("openid1")
      assertEquals("openid1", agg.wechatWxpaOpenId)
      val event = agg.domainEvents.last()
      assertTrue(event is WechatBoundEvent)
      assertEquals("openid1", (event as WechatBoundEvent).openId)
    }
    @Test
    fun `重复绑定微信抛出异常`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      agg.bindWechat("openid1")
      assertThrows<IllegalArgumentException> { agg.bindWechat("openid2") }
    }
  }

  @Nested
  inner class ChangePassword {
    @Test
    fun `正常修改密码`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      agg.changePassword("newpwd")
      assertEquals("newpwd", agg.passwordEnc)
      val event = agg.domainEvents.last()
      assertTrue(event is PasswordChangedEvent)
    }
  }

  @Nested
  inner class FavoritePost {
    @Test
    fun `添加收藏帖子`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      val postId = AggregateId.change("1234")
      agg.addFavoritePost(postId)
      assertTrue(agg.getFavoritePostIds().contains(postId))
      val event = agg.domainEvents.last()
      assertTrue(event is PostFavoritedEvent)
    }
    @Test
    fun `重复收藏不会重复`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      val postId = AggregateId.change("1234")
      agg.addFavoritePost(postId)
      agg.addFavoritePost(postId)
      assertEquals(1, agg.getFavoritePostIds().size)
    }
    @Test
    fun `移除收藏帖子`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      val postId = AggregateId.change("1234")
      agg.addFavoritePost(postId)
      agg.removeFavoritePost(postId)
      assertFalse(agg.getFavoritePostIds().contains(postId))
      val event2 = agg.domainEvents.last()
      assertTrue(event2 is PostUnfavoritedEvent)
    }
    @Test
    fun `重复移除不会触发事件`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("233"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      val postId = AggregateId.change("1234")
      agg.removeFavoritePost(postId)
      // 不抛异常，不触发事件
      assertTrue(agg.getFavoritePostIds().isEmpty())
    }
    @Test
    fun `获取收藏列表为空`() {
      val agg = UserAccountAggregate(
        id = AggregateId.change("2344"),
        account = "acc",
        passwordEnc = "pwd",
        nickName = "nick"
      )
      assertTrue(agg.getFavoritePostIds().isEmpty())
    }
  }
}