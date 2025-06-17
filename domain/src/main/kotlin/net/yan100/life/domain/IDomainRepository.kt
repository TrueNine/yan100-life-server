package net.yan100.life.domain

interface IDomainRepository<T : AggregateRoot> {
  suspend fun save(aggregate: T): T
  suspend fun findById(id: AggregateId.Query): T?
  suspend fun existsById(id: AggregateId.Query): Boolean
}
