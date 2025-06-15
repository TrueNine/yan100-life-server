package net.yan100.life.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DomainEventTest {
    @Test
    fun `test DomainEvent properties`() {
        val id = AggregateId.Create.Default("tenant1")
        val event = object : DomainEvent(aggregateId = id) {}
        assertNotNull(event.eventId)
        assertNotNull(event.occurredOn)
        assertEquals(id, event.aggregateId)
    }
} 