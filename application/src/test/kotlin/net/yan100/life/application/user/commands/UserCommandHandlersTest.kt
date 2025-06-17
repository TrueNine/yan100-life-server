package net.yan100.life.application.user.commands

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import net.yan100.compose.slf4j
import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.DomainEventPublisher
import net.yan100.life.domain.user.UserAccountAggregate
import net.yan100.life.domain.user.UserAccountRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UserCommandHandlersTest {

  companion object {
    @JvmStatic
    private val log = slf4j<UserCommandHandlersTest>()
  }

  private lateinit var userRepository: UserAccountRepository
  private lateinit var passwordEncoder: PasswordEncoder
  private lateinit var eventPublisher: DomainEventPublisher

  private lateinit var createUserAccountCommandHandler: CreateUserAccountCommandHandler
  private lateinit var updateUserProfileCommandHandler: UpdateUserProfileCommandHandler
  private lateinit var bindWechatCommandHandler: BindWechatCommandHandler
  private lateinit var changePasswordCommandHandler: ChangePasswordCommandHandler
  private lateinit var favoritePostCommandHandler: FavoritePostCommandHandler
  private lateinit var unfavoritePostCommandHandler: UnfavoritePostCommandHandler

  @BeforeEach
  fun setUp() {
    log.trace("[setUp] initializing mocks and handlers")
    userRepository = mockk()
    passwordEncoder = mockk()
    eventPublisher = mockk()
    createUserAccountCommandHandler = net.yan100.life.application.user.commands.CreateUserAccountCommandHandler(userRepository, passwordEncoder, eventPublisher)
    updateUserProfileCommandHandler = net.yan100.life.application.user.commands.UpdateUserProfileCommandHandler(userRepository, eventPublisher)
    bindWechatCommandHandler = net.yan100.life.application.user.commands.BindWechatCommandHandler(userRepository, eventPublisher)
    changePasswordCommandHandler = net.yan100.life.application.user.commands.ChangePasswordCommandHandler(userRepository, passwordEncoder, eventPublisher)
    favoritePostCommandHandler = net.yan100.life.application.user.commands.FavoritePostCommandHandler(userRepository, eventPublisher)
    unfavoritePostCommandHandler = net.yan100.life.application.user.commands.UnfavoritePostCommandHandler(userRepository, eventPublisher)
  }

  @AfterEach
  fun tearDown() {
    log.trace("[tearDown] clearing all mocks")
    clearAllMocks()
  }

  @Nested
  inner class CreateUserAccountTests {

    @Test
    fun `正常 创建用户账户成功`() = runTest {
      log.trace("[正常 创建用户账户成功] starting test")
      // given
      val command = CreateUserAccountCommand("testuser", "password", "Test User", "1234567890")
      val mockReturnedIdResult = mockk<AggregateId.Result>()
      val mockAggId = mockk<AggregateId>()
      every { mockAggId.toResultId() } returns mockReturnedIdResult
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { id } returns mockAggId
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.existsByAccount(command.account) } returns false
      coEvery { userRepository.existsByPhone(command.phone!!) } returns false
      every { passwordEncoder.encode(command.password) } returns "encodedPassword"
      coEvery { userRepository.save(any()) } returns mockUser
      every { eventPublisher.publishAllByAggregateRoot(mockUser) } returns Unit

      // when
      val result = createUserAccountCommandHandler.handle(command)

      // then
      coVerify { userRepository.existsByAccount(command.account) }
      coVerify { userRepository.existsByPhone(command.phone!!) }
      verify { passwordEncoder.encode(command.password) }
      coVerify { userRepository.save(any()) }
      verify { eventPublisher.publishAllByAggregateRoot(mockUser) }
      verify { mockUser.clearEvents() }
      verify { mockUser.id }
      verify { mockAggId.toResultId() }
      assertEquals(mockReturnedIdResult, result)

      confirmVerified(userRepository, passwordEncoder, eventPublisher, mockUser, mockAggId)
    }

    @Test
    fun `异常 账户已存在时抛出异常`() = runTest {
      log.trace("[异常 账户已存在时抛出异常] starting test")
      // given
      val command = CreateUserAccountCommand("testuser", "password", "Test User")
      coEvery { userRepository.existsByAccount(command.account) } returns true

      // when & then
      val ex = assertFailsWith<IllegalArgumentException> {
        createUserAccountCommandHandler.handle(command)
      }
      assertEquals("账号 ${command.account} 已存在", ex.message)
    }

    @Test
    fun `异常 手机号已存在时抛出异常`() = runTest {
      log.trace("[异常 手机号已存在时抛出异常] starting test")
      // given
      val command = CreateUserAccountCommand("testuser", "password", "Test User", "1234567890")
      coEvery { userRepository.existsByAccount(command.account) } returns false
      coEvery { userRepository.existsByPhone(command.phone!!) } returns true

      // when & then
      val ex = assertFailsWith<IllegalArgumentException> {
        createUserAccountCommandHandler.handle(command)
      }
      assertEquals("手机号 ${command.phone} 已被使用", ex.message)
    }

    @Test
    fun `正常 创建用户账户成功当phone为null时`() = runTest {
      log.trace("[正常 创建用户账户成功当phone为null时] starting test")
      // given
      val command = CreateUserAccountCommand("testuser", "password", "Test User", null)
      val mockReturnedIdResult = mockk<AggregateId.Result>()
      val mockAggId = mockk<AggregateId>()
      every { mockAggId.toResultId() } returns mockReturnedIdResult
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { id } returns mockAggId
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.existsByAccount(command.account) } returns false
      every { passwordEncoder.encode(command.password) } returns "encodedPassword"
      coEvery { userRepository.save(any()) } returns mockUser
      every { eventPublisher.publishAllByAggregateRoot(mockUser) } returns Unit

      // when
      val result = createUserAccountCommandHandler.handle(command)

      // then
      coVerify { userRepository.existsByAccount(command.account) }
      // 验证当phone为null时，不会调用existsByPhone
      coVerify(exactly = 0) { userRepository.existsByPhone(any()) }
      verify { passwordEncoder.encode(command.password) }
      coVerify { userRepository.save(any()) }
      verify { eventPublisher.publishAllByAggregateRoot(mockUser) }
      verify { mockUser.clearEvents() }
      verify { mockUser.id }
      verify { mockAggId.toResultId() }
      assertEquals(mockReturnedIdResult, result)

      confirmVerified(userRepository, passwordEncoder, eventPublisher, mockUser, mockAggId)
    }
  }

  @Nested
  inner class UpdateUserProfileTests {

    @Test
    fun `正常 更新用户资料成功`() = runTest {
      log.trace("[正常 更新用户资料成功] starting test")
      // given
      val userId = AggregateId.change("123")
      val command = UpdateUserProfileCommand(userId, "New Name", "0987654321", "new_avatar.jpg")
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      coEvery { userRepository.save(mockUser) } returns mockUser
      every { eventPublisher.publishAllByAggregateRoot(mockUser) } returns Unit

      // when
      updateUserProfileCommandHandler.handle(command)

      // then
      coVerify { userRepository.findById(userId.toQueryId()) }
      verify { mockUser.updateProfile(command.nickName, command.phone, command.avatarUrl) }
      coVerify { userRepository.save(mockUser) }
      verify { eventPublisher.publishAllByAggregateRoot(mockUser) }
      verify { mockUser.clearEvents() }

      confirmVerified(userRepository, eventPublisher, mockUser)
    }

    @Test
    fun `异常 用户不存在时抛出异常`() = runTest {
      log.trace("[异常 用户不存在时抛出异常] starting test")
      // given
      val userId = AggregateId.change("999")
      val command = UpdateUserProfileCommand(userId, "New Name")
      coEvery { userRepository.findById(userId.toQueryId()) } returns null

      // when & then
      val ex = assertFailsWith<IllegalStateException> {
        updateUserProfileCommandHandler.handle(command)
      }
      assertEquals("用户不存在: $userId", ex.message)
    }

    @Test
    fun `正常 更新用户资料成功使用默认参数`() = runTest {
      log.trace("[正常 更新用户资料成功使用默认参数] starting test")
      // given
      val userId = AggregateId.change("123")
      val command = UpdateUserProfileCommand(userId) // 只使用userId，其他参数使用默认值null
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      coEvery { userRepository.save(mockUser) } returns mockUser
      every { eventPublisher.publishAllByAggregateRoot(mockUser) } returns Unit

      // when
      updateUserProfileCommandHandler.handle(command)

      // then
      coVerify { userRepository.findById(userId.toQueryId()) }
      verify { mockUser.updateProfile(null, null, null) }
      coVerify { userRepository.save(mockUser) }
      verify { eventPublisher.publishAllByAggregateRoot(mockUser) }
      verify { mockUser.clearEvents() }

      confirmVerified(userRepository, eventPublisher, mockUser)
    }
  }

  @Nested
  inner class BindWechatTests {

    @Test
    fun `正常 绑定微信成功`() = runTest {
      log.trace("[正常 绑定微信成功] starting test")
      // given
      val userId = AggregateId.change("456")
      val command = BindWechatCommand(userId, "test_openid")
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.findByWechatOpenId(command.openId) } returns null
      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      coEvery { userRepository.save(mockUser) } returns mockUser
      every { eventPublisher.publishAll(mockUser.domainEvents) } returns Unit

      // when
      bindWechatCommandHandler.handle(command)

      // then
      coVerify { userRepository.findByWechatOpenId(command.openId) }
      coVerify { userRepository.findById(userId.toQueryId()) }
      verify { mockUser.bindWechat(command.openId) }
      coVerify { userRepository.save(mockUser) }
      verify { mockUser.domainEvents }
      verify { eventPublisher.publishAll(mockUser.domainEvents) }
      verify { mockUser.clearEvents() }

      confirmVerified(userRepository, eventPublisher, mockUser)
    }

    @Test
    fun `异常 微信已绑定其他账号时抛出异常`() = runTest {
      log.trace("[异常 微信已绑定其他账号时抛出异常] starting test")
      // given
      val userId = AggregateId.change("456")
      val command = BindWechatCommand(userId, "test_openid")
      val existingUser = mockk<UserAccountAggregate>()
      coEvery { userRepository.findByWechatOpenId(command.openId) } returns existingUser

      // when & then
      val ex = assertFailsWith<IllegalStateException> {
        bindWechatCommandHandler.handle(command)
      }
      assertEquals("该微信已绑定其他账号", ex.message)
    }

    @Test
    fun `异常 用户不存在时抛出异常`() = runTest {
      log.trace("[异常 用户不存在时抛出异常] starting test")
      // given
      val userId = AggregateId.change("999")
      val command = BindWechatCommand(userId, "test_openid")
      coEvery { userRepository.findByWechatOpenId(command.openId) } returns null
      coEvery { userRepository.findById(userId.toQueryId()) } returns null

      // when & then
      val ex = assertFailsWith<IllegalStateException> {
        bindWechatCommandHandler.handle(command)
      }
      assertEquals("用户不存在: $userId", ex.message)
    }
  }

  @Nested
  inner class ChangePasswordTests {

    @Test
    fun `正常 修改密码成功`() = runTest {
      log.trace("[正常 修改密码成功] starting test")
      // given
      val userId = AggregateId.change("789")
      val command = ChangePasswordCommand(userId, "oldPass", "newPass")
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { passwordEnc } returns "encodedOldPass"
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      every { passwordEncoder.matches(command.oldPassword, "encodedOldPass") } returns true
      every { passwordEncoder.encode(command.newPassword) } returns "encodedNewPass"
      coEvery { userRepository.save(mockUser) } returns mockUser
      every { eventPublisher.publishAll(mockUser.domainEvents) } returns Unit

      // when
      changePasswordCommandHandler.handle(command)

      // then
      coVerify { userRepository.findById(userId.toQueryId()) }
      verify { mockUser.passwordEnc }
      verify { passwordEncoder.matches(command.oldPassword, "encodedOldPass") }
      verify { passwordEncoder.encode(command.newPassword) }
      verify { mockUser.changePassword("encodedNewPass") }
      coVerify { userRepository.save(mockUser) }
      verify { mockUser.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockUser.clearEvents() }

      confirmVerified(userRepository, passwordEncoder, eventPublisher, mockUser)
    }

    @Test
    fun `异常 用户不存在时抛出异常`() = runTest {
      log.trace("[异常 用户不存在时抛出异常] starting test")
      // given
      val userId = AggregateId.change("999")
      val command = ChangePasswordCommand(userId, "oldPass", "newPass")
      coEvery { userRepository.findById(userId.toQueryId()) } returns null

      // when & then
      val ex = assertFailsWith<IllegalStateException> {
        changePasswordCommandHandler.handle(command)
      }
      assertEquals("用户不存在: $userId", ex.message)
    }

    @Test
    fun `异常 原密码不正确时抛出异常`() = runTest {
      log.trace("[异常 原密码不正确时抛出异常] starting test")
      // given
      val userId = AggregateId.change("789")
      val command = ChangePasswordCommand(userId, "wrongOldPass", "newPass")
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { passwordEnc } returns "encodedOldPass"
      }

      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      every { passwordEncoder.matches(command.oldPassword, "encodedOldPass") } returns false

      // when & then
      val ex = assertFailsWith<IllegalArgumentException> {
        changePasswordCommandHandler.handle(command)
      }
      assertEquals("原密码不正确", ex.message)
    }
  }

  @Nested
  inner class FavoritePostTests {

    @Test
    fun `正常 收藏帖子成功`() = runTest {
      log.trace("[正常 收藏帖子成功] starting test")
      // given
      val userId = AggregateId.change("101")
      val postId = AggregateId.change("202")
      val command = FavoritePostCommand(userId, postId)
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      coEvery { userRepository.save(mockUser) } returns mockUser
      every { eventPublisher.publishAll(mockUser.domainEvents) } returns Unit

      // when
      favoritePostCommandHandler.handle(command)

      // then
      coVerify { userRepository.findById(userId.toQueryId()) }
      verify { mockUser.addFavoritePost(any()) }
      coVerify { userRepository.save(mockUser) }
      verify { mockUser.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockUser.clearEvents() }

      confirmVerified(userRepository, eventPublisher, mockUser)
    }

    @Test
    fun `异常 用户不存在时抛出异常`() = runTest {
      log.trace("[异常 用户不存在时抛出异常] starting test")
      // given
      val userId = AggregateId.change("999")
      val postId = AggregateId.change("202")
      val command = FavoritePostCommand(userId, postId)
      coEvery { userRepository.findById(userId.toQueryId()) } returns null

      // when & then
      val ex = assertFailsWith<IllegalStateException> {
        favoritePostCommandHandler.handle(command)
      }
      assertEquals("用户不存在: $userId", ex.message)
    }
  }

  @Nested
  inner class UnfavoritePostTests {

    @Test
    fun `正常 取消收藏帖子成功`() = runTest {
      log.trace("[正常 取消收藏帖子成功] starting test")
      // given
      val userId = AggregateId.change("303")
      val postId = AggregateId.change("404")
      val command = UnfavoritePostCommand(userId, postId)
      val mockUser = mockk<UserAccountAggregate>(relaxed = true) {
        every { domainEvents } returns emptyList()
      }

      coEvery { userRepository.findById(userId.toQueryId()) } returns mockUser
      coEvery { userRepository.save(mockUser) } returns mockUser
      every { eventPublisher.publishAll(mockUser.domainEvents) } returns Unit

      // when
      unfavoritePostCommandHandler.handle(command)

      // then
      coVerify { userRepository.findById(userId.toQueryId()) }
      verify { mockUser.removeFavoritePost(postId) }
      coVerify { userRepository.save(mockUser) }
      verify { mockUser.domainEvents }
      verify { eventPublisher.publishAll(any()) }
      verify { mockUser.clearEvents() }

      confirmVerified(userRepository, eventPublisher, mockUser)
    }

    @Test
    fun `异常 用户不存在时抛出异常`() = runTest {
      log.trace("[异常 用户不存在时抛出异常] starting test")
      // given
      val userId = AggregateId.change("999")
      val postId = AggregateId.change("404")
      val command = UnfavoritePostCommand(userId, postId)
      coEvery { userRepository.findById(userId.toQueryId()) } returns null

      // when & then
      val ex = assertFailsWith<IllegalStateException> {
        unfavoritePostCommandHandler.handle(command)
      }
      assertEquals("用户不存在: $userId", ex.message)
    }
  }
} 
