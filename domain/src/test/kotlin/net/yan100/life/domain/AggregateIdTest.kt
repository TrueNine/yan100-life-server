package net.yan100.life.domain

import net.yan100.compose.datetime
import net.yan100.compose.toId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AggregateIdTest {
  @Test
  fun `test create AggregateId by string`() {
    val id = AggregateId.create("tenant1")
    assertNotNull(id)
    assertEquals("tenant1", id.tenantId)
  }

  @Test
  fun `test Query Default by string`() {
    val query = AggregateId.Query.Default("123", "tenant2")
    assertNotNull(query)
    assertEquals("tenant2", query.tenantId)
  }

  @Test
  fun `test Result Default by string`() {
    val now = datetime.now()
    val result = AggregateId.resultByStringId(
      stringId = "456",
      tenantId = "tenant3",
      createAt = now,
      updateAt = now,
      version = 1
    )
    assertEquals("tenant3", result.tenantId)
    assertEquals(1, result.version)
    assertEquals(now, result.createAt)
    assertEquals(now, result.updateAt)
  }

  @Test
  fun `test Change Default by string`() {
    val now = datetime.now()
    val change = AggregateId.Change.Default(
      id = "789".toId()!!,
      tenantId = "tenant4",
      updateAt = now,
      version = 2
    )
    assertEquals("tenant4", change.tenantId)
    assertEquals(2, change.version)
    assertEquals(now, change.updateAt)
  }

  @Test
  fun `test toQueryId toCreateId toChangeId toResultId`() {
    val now = datetime.now()
    val result = AggregateId.result(
      id = "234".toId()!!,
      tenantId = "tenant5",
      createAt = now,
      updateAt = now,
      version = 3
    )
    val query = result.toQueryId("tenant5")
    assertEquals("tenant5", query.tenantId)
    val create = result.toCreateId("tenant5", now)
    assertEquals("tenant5", create.tenantId)
    val change = result.toChangeId("tenant5", now, 3)
    assertEquals("tenant5", change.tenantId)
    val result2 = result.toResultId()
    assertEquals(result, result2)
  }

  @Test
  fun `test equals and hashCode`() {
    val now = datetime.now()
    val id1 = AggregateId.Result.Default(
      id = "123".toId()!!,
      tenantId = "t1",
      createAt = now,
      updateAt = now,
      version = 1
    )
    val id2 = AggregateId.Result.Default(
      id = "123".toId()!!,
      tenantId = "t1",
      createAt = now,
      updateAt = now,
      version = 1
    )
    assertEquals(id1, id2)
    assertEquals(id1.hashCode(), id2.hashCode())
  }

  @Test
  fun `test companion object methods`() {
    val now = datetime.now()
    val id = "234".toId()!!
    val result = AggregateId.result(id, "t3", now, now, 5)
    assertEquals("t3", result.tenantId)
    val change = AggregateId.change(id, "t3", now, 5)
    assertEquals("t3", change.tenantId)
    val change2 = AggregateId.change("234", "t3", now, 5)
    assertEquals("t3", change2.tenantId)
    val query = AggregateId.query(id, "t3")
    assertEquals("t3", query.tenantId)
    val create = AggregateId.create("t3", now)
    assertEquals("t3", create.tenantId)
  }

  @Test
  fun `test error on invalid id`() {
    assertThrows<IllegalStateException> {
      AggregateId.Query.Default("", "t")
    }
  }

  @Nested
  inner class Create {
    @Test
    fun `两个 不同的 create id 应当不相等` () {
      val  id1 = AggregateId.create("t1")
      val  id2 = AggregateId.create("t2")
      assertNotEquals(id1, id2)
    }
  }
}
