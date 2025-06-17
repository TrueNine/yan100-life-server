package net.yan100.life.application.content.commands

import net.yan100.life.application.CommandHandler
import net.yan100.life.application.CommandHandlerWithResult
import net.yan100.life.domain.content.PostContentAggregate
import net.yan100.life.domain.content.PostContentRepository
import net.yan100.life.domain.content.MessageAggregate
import net.yan100.life.domain.content.MessageRepository
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEventPublisher
import org.springframework.stereotype.Service

@Service
class CreatePostCommandHandler(
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandlerWithResult<CreatePostCommand, AggregateId> {

  override suspend fun handle(command: CreatePostCommand): AggregateId.Result {
    val post = PostContentAggregate.create(
      title = command.title,
      content = command.content,
      type = command.type,
      pubUserAccountId = command.pubUserAccountId.toChangeId()
    )

    val savedPost = postRepository.save(post)
    eventPublisher.publishAll(savedPost.domainEvents)
    savedPost.clearEvents()

    return savedPost.id.toResultId()
  }
}

@Service
class UpdatePostCommandHandler(
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<UpdatePostCommand> {

  override suspend fun handle(command: UpdatePostCommand) {
    val post = postRepository.findById(command.postId.toQueryId())
      ?: error("帖子不存在: ${command.postId}")

    // 验证权限
    require(post.pubUserAccountId == command.userId) {
      "无权修改此帖子"
    }

    post.update(
      title = command.title,
      content = command.content
    )

    postRepository.save(post)
    eventPublisher.publishAll(post.domainEvents)
    post.clearEvents()
  }
}

@Service
class SubmitPostForAuditCommandHandler(
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<SubmitPostForAuditCommand> {

  override suspend fun handle(command: SubmitPostForAuditCommand) {
    val post = postRepository.findById(command.postId.toQueryId())
      ?: error("帖子不存在: ${command.postId}")

    // 验证权限
    require(post.pubUserAccountId == command.userId) {
      "无权操作此帖子"
    }

    post.submitForAudit()

    postRepository.save(post)
    eventPublisher.publishAll(post.domainEvents)
    post.clearEvents()
  }
}

@Service
class ApprovePostCommandHandler(
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<ApprovePostCommand> {

  override suspend fun handle(command: ApprovePostCommand) {
    val post = postRepository.findById(command.postId.toQueryId())
      ?: error("帖子不存在: ${command.postId}")

    post.approve(command.auditorId.toChangeId())

    postRepository.save(post)
    eventPublisher.publishAll(post.domainEvents)
    post.clearEvents()
  }
}

@Service
class RejectPostCommandHandler(
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<RejectPostCommand> {

  override suspend fun handle(command: RejectPostCommand) {
    val post = postRepository.findById(command.postId.toQueryId())
      ?: error("帖子不存在: ${command.postId}")

    post.reject(command.auditorId, command.reason)
    postRepository.save(post)
    eventPublisher.publishAll(post.domainEvents)
    post.clearEvents()
  }
}

@Service
class RemovePostCommandHandler(
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<RemovePostCommand> {

  override suspend fun handle(command: RemovePostCommand) {
    val post = postRepository.findById(command.postId.toQueryId())
      ?: error("帖子不存在: ${command.postId}")

    // 验证权限 - 只有作者可以删除
    require(post.pubUserAccountId == command.userId) {
      "无权删除此帖子"
    }

    post.remove()

    postRepository.save(post)
    eventPublisher.publishAll(post.domainEvents)
    post.clearEvents()
  }
}

@Service
class SendMessageCommandHandler(
  private val messageRepository: MessageRepository,
  private val postRepository: PostContentRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandlerWithResult<SendMessageCommand, AggregateId> {

  override suspend fun handle(command: SendMessageCommand): AggregateId.Result {
    // 验证帖子是否存在
    val post = postRepository.findById(command.postId.toQueryId())
      ?: error("帖子不存在: ${command.postId}")

    // 验证帖子是否已发布
    require(post.isPublished()) {
      "只能对已发布的帖子发送消息"
    }

    // 创建消息
    val message = MessageAggregate.create(
      fromUserId = command.fromUserId,
      toUserId = command.toUserId,
      postId = command.postId,
      content = command.content
    )

    val savedMessage = messageRepository.save(message)
    eventPublisher.publishAll(savedMessage.domainEvents)
    savedMessage.clearEvents()

    return savedMessage.id.toResultId()
  }
} 
