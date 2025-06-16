package net.yan100.life.domain

import net.yan100.compose.slf4j

abstract class TypedAggregateRoot<Id : AggregateId>(
  open var id: Id,
) {
  open val activated: Boolean get() = id.activated

  init {
    log.trace("init typed aggregate root, id: {}", id)
  }

  @Transient
  private val _domainEvents = mutableListOf<DomainEvent>()

  val domainEvents: List<DomainEvent>
    get() = _domainEvents.toList()

  fun active(
    id: Id,
  ) {
    check(id.activated) { "id is not activated for $id" }
    this.id = id
  }

  protected fun checkActivatedOrThrow() {
    check(activated) { "aggregate is not activated for $this" }
  }

  protected fun <R : Any> checkActivatedOrThrow(provider: () -> R): R {
    check(id.activated) { "id is not activated" }
    return provider()
  }

  protected fun raiseEvent(event: DomainEvent) {
    log.trace("raise event: {}", event)
    _domainEvents += event
  }

  fun clearEvents() {
    log.trace("clear events for {}", this)
    _domainEvents.clear()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is TypedAggregateRoot<*>) return false
    if (id != other.id) return false
    if (_domainEvents != other._domainEvents) return false
    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + _domainEvents.hashCode()
    return result
  }

  override fun toString(): String {
    return "TypedAggregateRoot(id=$id, _domainEvents=$_domainEvents, domainEvents=$domainEvents)"
  }

  companion object {
    @JvmStatic
    private val log = slf4j<TypedAggregateRoot<*>>()
  }
}
