package net.yan100.life.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

  

class DomainEventPublisherTest {
    @Test
    fun `test publish and publishAll`() {
        val publisher = DummyEventPublisher()
        val id = AggregateId.Create.Default("tenant1")
        val event1 = object : DomainEvent(aggregateId = id) {}
        val event2 = object : DomainEvent(aggregateId = id) {}
        publisher.publish(event1)
        assertEquals(1, publisher.published.size)
        publisher.publishAll(listOf(event2))
        assertEquals(2, publisher.published.size)
        assertTrue(publisher.published.contains(event1))
        assertTrue(publisher.published.contains(event2))
    }
} 
