package net.yan100.life.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class AggregateIdFnsTest {
  @Test
  fun `test Long toAggregateChangeId`() {
    val id = 123L.toAggregateChangeId("tenant1")
    assertNotNull(id)
    assertEquals("tenant1", id.tenantId)
  }

  @Test
  fun `test String toAggregateChangeId`() {
    val id = "456".toAggregateChangeId("tenant2")
    assertNotNull(id)
    assertEquals("tenant2", id.tenantId)
  }
} 
