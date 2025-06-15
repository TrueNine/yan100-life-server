package net.yan100.life.domain

import java.time.Instant
import java.util.*

abstract class DomainEvent(
  val eventId: String = UUID.randomUUID().toString(),
  val occurredOn: Instant = Instant.now(),
  open val aggregateId: AggregateId,
)
