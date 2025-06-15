package net.yan100.life.domain.user

import net.yan100.life.domain.AggregateId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserAccountAggregateTest {
  @Test
  fun `test create`() {
    val agg = UserAccountAggregate.create("acc", "pwd", "nick", "13333333333")
    Assertions.assertEquals("acc", agg.account)
    Assertions.assertEquals("pwd", agg.passwordEnc)
    Assertions.assertEquals("nick", agg.nickName)
    Assertions.assertEquals("13333333333", agg.phone)
    Assertions.assertTrue(agg.domainEvents.first() is UserAccountCreatedEvent)
  }

  @Test
  fun `test updateProfile`() {
    val agg = UserAccountAggregate.create("acc", "pwd", "nick")
    agg.active(
      AggregateId.Companion.change(123)
    )
    agg.updateProfile(nickName = "newNick", phone = "18888888888", avatarUrl = "url")
    Assertions.assertEquals("newNick", agg.nickName)
    Assertions.assertEquals("18888888888", agg.phone)
    Assertions.assertEquals("url", agg.avatarUrl)
    val event = agg.domainEvents.last()
    Assertions.assertTrue(event is UserProfileUpdatedEvent)
    val e = event as UserProfileUpdatedEvent
    Assertions.assertEquals("newNick", e.nickName)
    Assertions.assertEquals("18888888888", e.phone)
    Assertions.assertEquals("url", e.avatarUrl)
  }

  @Test
  fun `test bindWechat success and fail`() {
    val agg = UserAccountAggregate.create("acc", "pwd", "nick")
    agg.active(
      AggregateId.Companion.change(123)
    )
    agg.bindWechat("openid1")
    Assertions.assertEquals("openid1", agg.wechatWxpaOpenId)
    val event = agg.domainEvents.last()
    Assertions.assertTrue(event is WechatBoundEvent)
    Assertions.assertEquals("openid1", (event as WechatBoundEvent).openId)
    assertThrows<IllegalArgumentException> { agg.bindWechat("openid2") }
  }

  @Test
  fun `test changePassword`() {
    val agg = UserAccountAggregate.create("acc", "pwd", "nick")
    agg.active(
      AggregateId.Companion.change(123)
    )

    agg.changePassword("newpwd")
    Assertions.assertEquals("newpwd", agg.passwordEnc)
    val event = agg.domainEvents.last()
    Assertions.assertTrue(event is PasswordChangedEvent)
  }

  @Test
  fun `test addFavoritePost and removeFavoritePost`() {
    val agg = UserAccountAggregate.create("acc", "pwd", "nick")
    agg.active(
      AggregateId.Companion.change(123)
    )
    val postId = AggregateId.Companion.change("1234")
    agg.addFavoritePost(postId)
    Assertions.assertTrue(agg.getFavoritePostIds().contains(postId))
    val event = agg.domainEvents.last()
    Assertions.assertTrue(event is PostFavoritedEvent)
    // 再次添加不会重复
    agg.addFavoritePost(postId)
    Assertions.assertEquals(1, agg.getFavoritePostIds().size)
    // 移除
    agg.removeFavoritePost(postId)
    Assertions.assertFalse(agg.getFavoritePostIds().contains(postId))
    val event2 = agg.domainEvents.last()
    Assertions.assertTrue(event2 is PostUnfavoritedEvent)
    // 再次移除不会触发事件
    agg.removeFavoritePost(postId)
  }

  @Test
  fun `test getFavoritePostIds empty`() {
    val agg = UserAccountAggregate.create("acc", "pwd", "nick")
    Assertions.assertTrue(agg.getFavoritePostIds().isEmpty())
  }
}
