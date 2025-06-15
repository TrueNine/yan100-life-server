package net.yan100.life.domain

class DummyRepository : IDomainRepository<DummyAggregateRoot> {
  var saved: DummyAggregateRoot? = null
  override suspend fun save(aggregate: DummyAggregateRoot): DummyAggregateRoot {
    saved = aggregate
    return aggregate
  }

  override suspend fun findById(id: AggregateId.Query): DummyAggregateRoot? {
    return saved?.takeIf { it.id == id }
  }
}

