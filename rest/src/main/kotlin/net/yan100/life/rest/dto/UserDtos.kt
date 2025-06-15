package net.yan100.life.rest.dto

// 请求DTO
data class RegisterDto(
  val account: String,
  val password: String,
  val nickName: String,
  val phone: String? = null,
)

data class UpdateProfileDto(
  val nickName: String? = null,
  val phone: String? = null,
  val avatarUrl: String? = null,
)

data class ChangePasswordDto(
  val oldPassword: String,
  val newPassword: String,
)

data class BindWechatDto(
  val openId: String,
)

// 响应DTO
data class RegisterView(
  val userId: String,
)

data class UserDetailView(
  val id: String,
  val account: String,
  val nickName: String,
  val phone: String? = null,
  val avatarUrl: String? = null,
  val hasWechatBound: Boolean = false,
  val favoritePostIds: List<String> = emptyList(),
)
