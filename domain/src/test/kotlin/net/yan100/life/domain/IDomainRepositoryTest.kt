package net.yan100.life.domain

import kotlinx.coroutines.runBlocking
import kotlin.test.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class IDomainRepositoryTest {
  @Nested
  inner class Save {
    @Test
    fun `保存后返回原对象`() = runBlocking {
      val repo = DummyRepository()
      val id = AggregateId.queryByStringId("100", "tenant1")
      val agg = DummyAggregateRoot(id)
      val saved = repo.save(agg)
      assertSame(agg, saved)
    }
  }

  @Nested
  inner class FindById {
    @Test
    fun `已保存id可查找`() = runBlocking {
      val repo = DummyRepository()
      val id = AggregateId.queryByStringId("100", "tenant1")
      val agg = DummyAggregateRoot(id)
      repo.save(agg)
      val found = repo.findById(id)
      assertNotNull(found)
      assertEquals(id, found!!.id)
    }
    @Test
    fun `未保存id查找返回null`() = runBlocking {
      val repo = DummyRepository()
      val notFoundId = AggregateId.queryByStringId("101", "tenant1")
      val notFound = repo.findById(notFoundId)
      assertNull(notFound)
    }
  }

  @Nested
  inner class ExistsById {
    @Test
    fun `已保存id存在`() = runBlocking {
      val repo = DummyRepository()
      val id = AggregateId.queryByStringId("100", "tenant1")
      val agg = DummyAggregateRoot(id)
      repo.save(agg)
      assertTrue(repo.existsById(id))
    }
    @Test
    fun `未保存id不存在`() = runBlocking {
      val repo = DummyRepository()
      val notFoundId = AggregateId.queryByStringId("101", "tenant1")
      assertFalse(repo.existsById(notFoundId))
    }
  }

  @Nested
  inner class 再次保存与查找 {
    @Test
    fun `保存后再次查找`() = runBlocking {
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
} 
