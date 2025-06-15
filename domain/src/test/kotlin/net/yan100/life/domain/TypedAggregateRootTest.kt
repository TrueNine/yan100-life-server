package net.yan100.life.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test



class TypedAggregateRootTest {
  @Test
  fun `test event collection and clear`() {
    val id = AggregateId.Create.Default("tenant1")
    val root = DummyAggregateRoot(id)
    val event = object : DomainEvent(aggregateId = id) {}
    root.emitEventTest(event)
    assertEquals(1, root.domainEvents.size)
    assertEquals(event, root.domainEvents[0])
    root.clearEvents()
    assertTrue(root.domainEvents.isEmpty())
  }

  @Test
  fun `test equals and hashCode`() {
    val id = AggregateId.Create.Default("tenant2")
    val root1 = DummyAggregateRoot(id)
    val root2 = DummyAggregateRoot(id)
    assertEquals(root1, root2)
    assertEquals(root1.hashCode(), root2.hashCode())
  }

  @Test
  fun `test toString`() {
    val id = AggregateId.Create.Default("tenant3")
    val root = DummyAggregateRoot(id)
    assertTrue(root.toString().contains("TypedAggregateRoot"))
  }
}
