package net.yan100.life.domain

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test


class IDomainRepositoryTest {
  @Test
  fun `test save and findById`() = runBlocking {
    val repo = DummyRepository()
    val id = AggregateId.queryByStringId("123", "tenant1")
    val agg = DummyAggregateRoot(id)
    val saved = repo.save(agg)
    assertEquals(agg, saved)
    val found = repo.findById(id)
    assertEquals(agg, found)
    val notFound = repo.findById(AggregateId.queryByStringId("1234", "tenant1"))
    assertNull(notFound)
  }
} 
