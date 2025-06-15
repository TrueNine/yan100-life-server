package net.yan100.life.domain

import net.yan100.compose.slf4j

abstract class AggregateRoot(
  override var id: AggregateId,
) : TypedAggregateRoot<AggregateId>(id) {

  init {
    log.info("AggregateRoot init id= {}",id)
  }

  companion object {
    @JvmStatic
    private val log = slf4j<AggregateRoot>()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AggregateRoot) return false
    if (id != other.id) return false
    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
