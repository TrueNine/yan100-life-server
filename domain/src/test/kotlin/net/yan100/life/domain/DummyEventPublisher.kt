package net.yan100.life.domain

class DummyEventPublisher : DomainEventPublisher {
  val published = mutableListOf<DomainEvent>()
  override fun publish(event: DomainEvent) {
    published += event
  }

  override fun publishAll(events: List<DomainEvent>) {
    published += events
  }
}
