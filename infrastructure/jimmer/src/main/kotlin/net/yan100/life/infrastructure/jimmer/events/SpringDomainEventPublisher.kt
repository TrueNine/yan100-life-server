package net.yan100.life.infrastructure.jimmer.events

import net.yan100.life.domain.DomainEvent
import net.yan100.life.domain.DomainEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SpringDomainEventPublisher(
  private val applicationEventPublisher: ApplicationEventPublisher,
) : DomainEventPublisher {
  override fun publish(event: DomainEvent) {
    applicationEventPublisher.publishEvent(event)
  }
} 
