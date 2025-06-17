package net.yan100.life.application.dto

import net.yan100.compose.slf4j
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QueryDtosTest {

  companion object {
    @JvmStatic
    private val log = slf4j<QueryDtosTest>()
  }

  @Nested
  inner class UserViewTests {

    @Test
    fun `正常 创建UserView对象`() {
      log.trace("[正常 创建UserView对象] starting test")
      // given & when
      val userView = UserView(
        id = "123",
        account = "testuser",
        nickName = "Test User",
        phone = "1234567890",
        avatarUrl = "avatar.jpg",
        hasWechatBound = true
      )

      // then
      assertEquals("123", userView.id)
      assertEquals("testuser", userView.account)
      assertEquals("Test User", userView.nickName)
      assertEquals("1234567890", userView.phone)
      assertEquals("avatar.jpg", userView.avatarUrl)
      assertTrue(userView.hasWechatBound)
    }

    @Test
    fun `正常 创建UserView对象使用默认值`() {
      log.trace("[正常 创建UserView对象使用默认值] starting test")
      // given & when
      val userView = UserView(
        id = "456",
        account = "testuser2",
        nickName = "Test User 2"
      )

      // then
      assertEquals("456", userView.id)
      assertEquals("testuser2", userView.account)
      assertEquals("Test User 2", userView.nickName)
      assertEquals(null, userView.phone)
      assertEquals(null, userView.avatarUrl)
      assertFalse(userView.hasWechatBound)
    }
  }

  @Nested
  inner class UserDetailDtoTests {

    @Test
    fun `正常 创建UserDetailDto对象`() {
      log.trace("[正常 创建UserDetailDto对象] starting test")
      // given & when
      val userDetailDto = UserDetailDto(
        id = "789",
        account = "detailuser",
        nickName = "Detail User",
        phone = "0987654321",
        avatarUrl = "detail_avatar.jpg",
        hasWechatBound = true,
        favoritePostIds = listOf("post1", "post2")
      )

      // then
      assertEquals("789", userDetailDto.id)
      assertEquals("detailuser", userDetailDto.account)
      assertEquals("Detail User", userDetailDto.nickName)
      assertEquals("0987654321", userDetailDto.phone)
      assertEquals("detail_avatar.jpg", userDetailDto.avatarUrl)
      assertTrue(userDetailDto.hasWechatBound)
      assertEquals(listOf("post1", "post2"), userDetailDto.favoritePostIds)
    }

    @Test
    fun `正常 创建UserDetailDto对象使用默认值`() {
      log.trace("[正常 创建UserDetailDto对象使用默认值] starting test")
      // given & when
      val userDetailDto = UserDetailDto(
        id = "101",
        account = "defaultuser",
        nickName = "Default User"
      )

      // then
      assertEquals("101", userDetailDto.id)
      assertEquals("defaultuser", userDetailDto.account)
      assertEquals("Default User", userDetailDto.nickName)
      assertEquals(null, userDetailDto.phone)
      assertEquals(null, userDetailDto.avatarUrl)
      assertFalse(userDetailDto.hasWechatBound)
      assertEquals(emptyList(), userDetailDto.favoritePostIds)
    }
  }

  @Nested
  inner class PostDtoTests {

    @Test
    fun `正常 创建PostDto对象`() {
      log.trace("[正常 创建PostDto对象] starting test")
      // given
      val publisher = UserView(
        id = "pub123",
        account = "publisher",
        nickName = "Publisher"
      )

      // when
      val postDto = PostDto(
        id = "post123",
        title = "Test Post",
        content = "Test Content",
        type = PostMessageType.JOB,
        status = PostContentStatus.AUDIT_PASS,
        publisher = publisher
      )

      // then
      assertEquals("post123", postDto.id)
      assertEquals("Test Post", postDto.title)
      assertEquals("Test Content", postDto.content)
      assertEquals(PostMessageType.JOB, postDto.type)
      assertEquals(PostContentStatus.AUDIT_PASS, postDto.status)
      assertEquals(publisher, postDto.publisher)
    }
  }

  @Nested
  inner class PostListItemViewTests {

    @Test
    fun `正常 创建PostListItemView对象`() {
      log.trace("[正常 创建PostListItemView对象] starting test")
      // given & when
      val postListItem = PostListItemView(
        id = "item123",
        title = "List Item Title",
        type = PostMessageType.HELP,
        status = PostContentStatus.NONE,
        publisherName = "Publisher Name",
        publisherId = "pub456"
      )

      // then
      assertEquals("item123", postListItem.id)
      assertEquals("List Item Title", postListItem.title)
      assertEquals(PostMessageType.HELP, postListItem.type)
      assertEquals(PostContentStatus.NONE, postListItem.status)
      assertEquals("Publisher Name", postListItem.publisherName)
      assertEquals("pub456", postListItem.publisherId)
    }
  }

  @Nested
  inner class MessageDtoTests {

    @Test
    fun `正常 创建MessageDto对象`() {
      log.trace("[正常 创建MessageDto对象] starting test")
      // given
      val fromUser = UserView(
        id = "from123",
        account = "fromuser",
        nickName = "From User"
      )
      val toUser = UserView(
        id = "to123",
        account = "touser",
        nickName = "To User"
      )
      val post = PostListItemView(
        id = "post123",
        title = "Message Post",
        type = PostMessageType.JOB,
        status = PostContentStatus.AUDIT_PASS,
        publisherName = "Publisher",
        publisherId = "pub123"
      )

      // when
      val messageDto = MessageDto(
        id = "msg123",
        content = "Message Content",
        fromUser = fromUser,
        toUser = toUser,
        post = post,
        read = false
      )

      // then
      assertEquals("msg123", messageDto.id)
      assertEquals("Message Content", messageDto.content)
      assertEquals(fromUser, messageDto.fromUser)
      assertEquals(toUser, messageDto.toUser)
      assertEquals(post, messageDto.post)
      assertFalse(messageDto.read)
    }
  }

  @Nested
  inner class AuditDtoTests {

    @Test
    fun `正常 创建AuditDto对象`() {
      log.trace("[正常 创建AuditDto对象] starting test")
      // given
      val auditor = UserView(
        id = "auditor123",
        account = "auditor",
        nickName = "Auditor"
      )

      // when
      val auditDto = AuditDto(
        id = "audit123",
        postId = "post123",
        postTitle = "Audit Post",
        auditor = auditor,
        status = PostContentStatus.AUDIT_FAIL,
        reason = "Content violation"
      )

      // then
      assertEquals("audit123", auditDto.id)
      assertEquals("post123", auditDto.postId)
      assertEquals("Audit Post", auditDto.postTitle)
      assertEquals(auditor, auditDto.auditor)
      assertEquals(PostContentStatus.AUDIT_FAIL, auditDto.status)
      assertEquals("Content violation", auditDto.reason)
    }

    @Test
    fun `正常 创建AuditDto对象使用默认值`() {
      log.trace("[正常 创建AuditDto对象使用默认值] starting test")
      // given
      val auditor = UserView(
        id = "auditor456",
        account = "auditor2",
        nickName = "Auditor 2"
      )

      // when
      val auditDto = AuditDto(
        id = "audit456",
        postId = "post456",
        postTitle = "Audit Post 2",
        auditor = auditor,
        status = PostContentStatus.AUDIT_PASS
      )

      // then
      assertEquals("audit456", auditDto.id)
      assertEquals("post456", auditDto.postId)
      assertEquals("Audit Post 2", auditDto.postTitle)
      assertEquals(auditor, auditDto.auditor)
      assertEquals(PostContentStatus.AUDIT_PASS, auditDto.status)
      assertEquals(null, auditDto.reason)
    }
  }

  @Nested
  inner class PageDtoTests {

    @Test
    fun `正常 创建PageDto对象`() {
      log.trace("[正常 创建PageDto对象] starting test")
      // given
      val content = listOf("item1", "item2", "item3")

      // when
      val pageDto = PageDto(
        content = content,
        pageNumber = 0,
        pageSize = 10,
        totalElements = 25L,
        totalPages = 3
      )

      // then
      assertEquals(content, pageDto.content)
      assertEquals(0, pageDto.pageNumber)
      assertEquals(10, pageDto.pageSize)
      assertEquals(25L, pageDto.totalElements)
      assertEquals(3, pageDto.totalPages)
    }
  }
} 