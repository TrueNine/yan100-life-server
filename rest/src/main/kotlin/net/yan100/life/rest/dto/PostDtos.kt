package net.yan100.life.rest.dto

import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType

// 请求DTO
data class CreatePostDto(
  val title: String,
  val content: String,
  val type: PostMessageType,
  val userId: String,
)

data class UpdatePostDto(
  val title: String? = null,
  val content: String? = null,
)

// 响应DTO
data class CreatePostView(
  val postId: String,
)

data class PostDetailView(
  val id: String,
  val title: String,
  val content: String,
  val type: PostMessageType,
  val status: PostContentStatus,
  val publisher: UserView,
)

data class PostListItemView(
  val id: String,
  val title: String,
  val type: PostMessageType,
  val status: PostContentStatus,
  val publisherName: String,
  val publisherId: String,
)

data class UserView(
  val id: String,
  val account: String,
  val nickName: String,
  val avatarUrl: String? = null,
)

data class PageView<T>(
  val content: List<T>,
  val pageNumber: Int,
  val pageSize: Int,
  val totalElements: Long,
  val totalPages: Int,
)
