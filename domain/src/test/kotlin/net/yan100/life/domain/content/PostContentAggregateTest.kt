package net.yan100.life.domain.content

import net.yan100.life.domain.AggregateId
import net.yan100.life.domain.enums.PostContentStatus
import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PostContentAggregateTest {
  private fun newAgg(): PostContentAggregate {
    val agg = PostContentAggregate.create(
      title = "t1",
      content = "c1",
      type = PostMessageType.SYS,
      pubUserAccountId = AggregateId.Companion.change("123")
    )
    agg.active(
      id = AggregateId.Companion.change("123")
    )
    return agg
  }

  @Test
  fun `test create`() {
    val agg = newAgg()
    assertEquals("t1", agg.title)
    assertEquals("c1", agg.content)
    assertEquals(PostMessageType.SYS, agg.type)
    assertEquals(PostContentStatus.PRE_AUDIT, agg.status)
    assertTrue(agg.domainEvents.first() is PostContentCreatedEvent)
  }

  @Test
  fun `test update success and fail`() {
    val agg = newAgg()
    agg.update(title = "t2", content = "c2")
    assertEquals("t2", agg.title)
    assertEquals("c2", agg.content)
    assertEquals(PostContentStatus.PRE_AUDIT, agg.status)
    val event = agg.domainEvents.last()
    assertTrue(event is PostContentUpdatedEvent)
    // 审核通过后不能再修改
    agg.status = PostContentStatus.AUDIT_PASS
    assertThrows<IllegalArgumentException> { agg.update("t3", "c3") }
  }

  @Test
  fun `test submitForAudit success and fail`() {
    val agg = newAgg()
    agg.submitForAudit()
    assertEquals(PostContentStatus.PRE_AUDIT, agg.status)
    val event = agg.domainEvents.last()
    assertTrue(event is PostContentSubmittedForAuditEvent)
    // 审核通过后不能再提交
    agg.status = PostContentStatus.AUDIT_PASS
    assertThrows<IllegalArgumentException> { agg.submitForAudit() }
  }

  @Test
  fun `test approve success and fail`() {
    val agg = newAgg()

    agg.approve(
      AggregateId.Companion.change("1234567")
    )
    assertEquals(PostContentStatus.AUDIT_PASS, agg.status)
    val event = agg.domainEvents.last()
    assertTrue(event is PostContentApprovedEvent)
    // 只有 PRE_AUDIT 才能通过
    agg.status = PostContentStatus.AUDIT_FAIL
    assertThrows<IllegalArgumentException> { agg.approve(AggregateId.Companion.change("123")) }
  }

  @Test
  fun `test reject success and fail`() {
    val agg = newAgg()
    agg.reject(AggregateId.Companion.change("123"), "reason")
    assertEquals(PostContentStatus.AUDIT_FAIL, agg.status)
    val event = agg.domainEvents.last()
    assertTrue(event is PostContentRejectedEvent)
    // 只有 PRE_AUDIT 才能拒绝
    agg.status = PostContentStatus.AUDIT_PASS
    assertThrows<IllegalArgumentException> { agg.reject(AggregateId.Companion.change("1234"), "reason") }
  }

  @Test
  fun `test remove success and fail`() {
    val agg = newAgg()
    agg.remove()
    assertEquals(PostContentStatus.REMOVED, agg.status)
    val event = agg.domainEvents.last()
    assertTrue(event is PostContentRemovedEvent)
    // 已删除不能再删
    assertThrows<IllegalArgumentException> { agg.remove() }
  }

  @Test
  fun `test canEdit and isPublished`() {
    val agg = newAgg()
    assertTrue(agg.canEdit())
    agg.status = PostContentStatus.AUDIT_PASS
    assertFalse(agg.canEdit())
    assertTrue(agg.isPublished())
    agg.status = PostContentStatus.PRE_AUDIT
    assertFalse(agg.isPublished())
  }
}
