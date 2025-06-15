package net.yan100.life.rest.adapters

import net.yan100.compose.Pq
import net.yan100.compose.Pr
import net.yan100.compose.toId
import net.yan100.life.application.CommandBus
import net.yan100.life.application.QueryBus
import net.yan100.life.application.content.commands.CreatePostCommand
import net.yan100.life.application.content.commands.RemovePostCommand
import net.yan100.life.application.content.commands.SubmitPostForAuditCommand
import net.yan100.life.application.content.commands.UpdatePostCommand
import net.yan100.life.application.content.queries.GetPostDetailQuery
import net.yan100.life.application.content.queries.GetPostsQuery
import net.yan100.life.application.content.queries.GetUserPostsQuery
import net.yan100.life.application.dto.PostListItemView
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import net.yan100.life.domain.toAggregateChangeId
import net.yan100.life.rest.dto.*
import org.springframework.web.bind.annotation.*

/**
 * 用户帖子接口
 */
@RestController
@RequestMapping("v1/posts")
class PostApi(
  private val commandBus: CommandBus,
  private val queryBus: QueryBus,
) {

  /**
   * 创建帖子
   */
  @PostMapping
  suspend fun createPost(@RequestBody request: CreatePostDto): CreatePostView {
    val postId = commandBus.send(
      CreatePostCommand(
        title = request.title,
        content = request.content,
        type = request.type,
        pubUserAccountId = AggregateId.change(stringId = request.userId),
      )
    )
    return CreatePostView(postId = postId.toString())
  }

  /**
   * 获取帖子详情
   */
  @GetMapping("/{postId}")
  suspend fun getPostDetail(@PathVariable postId: String): PostDetailView? {
    val post = queryBus.send(GetPostDetailQuery(postId.toId()!!))
    return post?.let {
      PostDetailView(
        id = it.id,
        title = it.title,
        content = it.content,
        type = it.type,
        status = it.status,
        publisher = UserView(
          id = it.publisher.id,
          account = it.publisher.account,
          nickName = it.publisher.nickName,
          avatarUrl = it.publisher.avatarUrl
        ),
      )
    }
  }

  /**
   * 更新帖子
   */
  @PutMapping("{postId}")
  suspend fun updatePost(
    @PathVariable postId: String,
    @RequestBody request: UpdatePostDto,
    @RequestHeader("X-User-Id") userId: String,
  ) {
    commandBus.send(
      UpdatePostCommand(
        postId = postId.toAggregateChangeId(),
        title = request.title,
        content = request.content,
        userId = userId.toAggregateChangeId()
      )
    )
  }

  /**
   * 提交审核
   */
  @PostMapping("{postId}/audit")
  suspend fun submitForAudit(
    @PathVariable postId: String,
    @RequestHeader("X-User-Id") userId: String,
  ) {
    commandBus.send(
      SubmitPostForAuditCommand(
        postId = postId.toAggregateChangeId(),
        userId = userId.toAggregateChangeId()
      )
    )
  }

  /**
   * 删除帖子
   */
  @DeleteMapping("{postId}")
  suspend fun removePost(
    @PathVariable postId: String,
    @RequestHeader("X-User-Id") userId: String,
  ) {
    commandBus.send(
      RemovePostCommand(
        postId = postId.toAggregateChangeId(),
        userId = userId.toAggregateChangeId()
      )
    )
  }

  /**
   * 获取帖子列表
   */
  @GetMapping
  suspend fun getPosts(
    @RequestParam(required = false) status: PostContentStatus?,
    @RequestParam(required = false) type: PostMessageType?,
    @RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "20") size: Int,
  ): Pr<PostListItemView> {
    return queryBus.send(
      GetPostsQuery(
        status = status,
        type = type,
        pq = Pq[page, size]
      )
    )
  }

  /**
   * 获取用户帖子
   */
  @GetMapping("user/{userId}")
  suspend fun getUserPosts(
    @PathVariable userId: String,
    @RequestParam(required = false) status: PostContentStatus?,
    @RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "20") size: Int,
  ): Pr<PostListItemView> {
    return queryBus.send(
      GetUserPostsQuery(
        userId = userId.toId()!!,
        status = status,
        pq = Pq[page, size]
      )
    )
  }
}
