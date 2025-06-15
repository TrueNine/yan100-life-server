package net.yan100.life.domain.user

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.AggregateRoot

/**
 * 用户账户聚合根
 */
class UserAccountAggregate(
  override var id: AggregateId,
  var account: String,
  var passwordEnc: String,
  var nickName: String,
  var phone: String? = null,
  var avatarUrl: String? = null,
  var wechatWxpaOpenId: String? = null,
  private val favoritePostContentIds: MutableSet<AggregateId> = mutableSetOf(),
) : AggregateRoot(id) {

  companion object {
    fun create(
      account: String,
      passwordEnc: String,
      nickName: String,
      phone: String? = null,
    ): UserAccountAggregate {
      val userId = AggregateId.Create.Default()
      val user = UserAccountAggregate(
        id = userId,
        account = account,
        passwordEnc = passwordEnc,
        nickName = nickName,
        phone = phone
      )
      user.raiseEvent(UserAccountCreatedEvent(userId, account, nickName))
      return user
    }
  }

  fun updateProfile(
    nickName: String? = null,
    phone: String? = null,
    avatarUrl: String? = null,
  ) {
    val oldNickName = this.nickName
    nickName?.let { this.nickName = it }
    phone?.let { this.phone = it }
    avatarUrl?.let { this.avatarUrl = it }

    raiseEvent(
      UserProfileUpdatedEvent(
        aggregateId = id.toChangeId(),
        nickName = nickName,
        phone = phone,
        avatarUrl = avatarUrl
      )
    )
  }

  fun bindWechat(openId: String) {
    require(wechatWxpaOpenId == null) { "微信已绑定" }
    wechatWxpaOpenId = openId
    raiseEvent(WechatBoundEvent(id.toChangeId(), openId))
  }

  fun changePassword(newPasswordEnc: String) {
    passwordEnc = newPasswordEnc
    raiseEvent(PasswordChangedEvent(id.toChangeId()))
  }

  fun addFavoritePost(postId: AggregateId.Change) {
    if (favoritePostContentIds.add(postId)) {
      raiseEvent(PostFavoritedEvent(id.toChangeId(), postId.toChangeId()))
    }
  }

  fun removeFavoritePost(postId: AggregateId.Change) {
    if (favoritePostContentIds.remove(postId)) {
      raiseEvent(PostUnfavoritedEvent(id.toChangeId(), postId))
    }
  }

  fun getFavoritePostIds(): Set<AggregateId> = favoritePostContentIds.toSet()
} 
