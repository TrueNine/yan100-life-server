package net.yan100.life.domain

/**
 * 事件发布者接口
 */
interface DomainEventPublisher {
  fun publish(event: DomainEvent)
  fun publishAll(events: List<DomainEvent>) {
    events.forEach {
      publish(event = it)
    }
  }

  fun publishAllByAggregateRoot(aggregateRoot: TypedAggregateRoot<*>) {
    this.publishAll(aggregateRoot.domainEvents)
  }

  fun publishAllByAggregateRoots(aggregateRoots: List<TypedAggregateRoot<*>>) {
    aggregateRoots.map { it.domainEvents }.flatten().forEach {
      publish(event = it)
    }
  }
}
