package net.yan100.life.domain

/**
 * 事件发布者接口
 */
interface DomainEventPublisher {
  fun publish(event: DomainEvent)
  fun publishAll(events: List<DomainEvent>)
}
