package net.yan100.life.application.user.commands

import net.yan100.life.application.CommandHandler
import net.yan100.life.application.CommandHandlerWithResult
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEventPublisher
import net.yan100.life.domain.user.UserAccountAggregate
import net.yan100.life.domain.user.UserAccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateUserAccountCommandHandler(
  private val userRepository: UserAccountRepository,
  private val passwordEncoder: PasswordEncoder,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandlerWithResult<CreateUserAccountCommand, AggregateId> {

  override suspend fun handle(command: CreateUserAccountCommand): AggregateId.Result {
    // 检查账号是否已存在
    require(!userRepository.existsByAccount(command.account)) {
      "账号 ${command.account} 已存在"
    }

    // 检查手机号是否已存在
    command.phone?.let {
      require(!userRepository.existsByPhone(it)) {
        "手机号 $it 已被使用"
      }
    }

    // 创建用户聚合
    val user = UserAccountAggregate.create(
      account = command.account,
      passwordEnc = passwordEncoder.encode(command.password),
      nickName = command.nickName,
      phone = command.phone
    )

    // 保存用户
    val savedUser = userRepository.save(user)

    // 发布领域事件
    eventPublisher.publishAll(savedUser.domainEvents)
    savedUser.clearEvents()

    return savedUser.id.toResultId()
  }
}

@Service
class UpdateUserProfileCommandHandler(
  private val userRepository: UserAccountRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<UpdateUserProfileCommand> {

  override suspend fun handle(command: UpdateUserProfileCommand) {
    val user = userRepository.findById(command.userId.toQueryId())
      ?: error("用户不存在: ${command.userId}")

    user.updateProfile(
      nickName = command.nickName,
      phone = command.phone,
      avatarUrl = command.avatarUrl
    )

    userRepository.save(user)
    eventPublisher.publishAll(user.domainEvents)
    user.clearEvents()
  }
}

@Service
class BindWechatCommandHandler(
  private val userRepository: UserAccountRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<BindWechatCommand> {

  override suspend fun handle(command: BindWechatCommand) {
    // 检查OpenId是否已被绑定
    userRepository.findByWechatOpenId(command.openId)?.let {
      error("该微信已绑定其他账号")
    }

    val user = userRepository.findById(command.userId.toQueryId())
      ?: error("用户不存在: ${command.userId}")

    user.bindWechat(command.openId)

    userRepository.save(user)
    eventPublisher.publishAll(user.domainEvents)
    user.clearEvents()
  }
}

@Service
class ChangePasswordCommandHandler(
  private val userRepository: UserAccountRepository,
  private val passwordEncoder: PasswordEncoder,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<ChangePasswordCommand> {

  override suspend fun handle(command: ChangePasswordCommand) {
    val user = userRepository.findById(command.userId.toQueryId())
      ?: error("用户不存在: ${command.userId}")

    // 验证旧密码
    require(passwordEncoder.matches(command.oldPassword, user.passwordEnc)) {
      "原密码不正确"
    }

    user.changePassword(passwordEncoder.encode(command.newPassword))

    userRepository.save(user)
    eventPublisher.publishAll(user.domainEvents)
    user.clearEvents()
  }
}

@Service
class FavoritePostCommandHandler(
  private val userRepository: UserAccountRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<FavoritePostCommand> {

  override suspend fun handle(command: FavoritePostCommand) {
    val user = userRepository.findById(command.userId.toQueryId())
      ?: error("用户不存在: ${command.userId}")

    user.addFavoritePost(command.postId.toChangeId())

    userRepository.save(user)
    eventPublisher.publishAll(user.domainEvents)
    user.clearEvents()
  }
}

@Service
class UnfavoritePostCommandHandler(
  private val userRepository: UserAccountRepository,
  private val eventPublisher: DomainEventPublisher,
) : CommandHandler<UnfavoritePostCommand> {

  override suspend fun handle(command: UnfavoritePostCommand) {
    val user = userRepository.findById(command.userId.toQueryId())
      ?: error("用户不存在: ${command.userId}")

    user.removeFavoritePost(command.postId)

    userRepository.save(user)
    eventPublisher.publishAll(user.domainEvents)
    user.clearEvents()
  }
} 
