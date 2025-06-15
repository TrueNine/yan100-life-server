package net.yan100.life.domain

class DummyAggregateRoot(
  id: AggregateId,
) : AggregateRoot(id) {
  fun emitEventTest(
    event: DomainEvent,
  ) {
    this.raiseEvent(event)
  }
}
