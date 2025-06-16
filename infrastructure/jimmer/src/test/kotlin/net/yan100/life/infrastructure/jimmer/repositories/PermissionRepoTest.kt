package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.testtoolkit.testcontainers.IDatabasePostgresqlContainer
import net.yan100.life.infrastructure.jimmer.entities.Permission
import net.yan100.life.infrastructure.jimmer.entities.by
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class PermissionRepoTest @Autowired constructor(
  val repo: IPermissionRepo,
) : IDatabasePostgresqlContainer {
  @Test
  fun `test create and find permission`() {
    val saved = repo.save(Permission {
      id = 1L
      code = "perm.test"
      name = "测试权限"
      description = "单元测试用"
    })
    val fetcher = newFetcher(Permission::class).by { allScalarFields() }
    val found = repo.findById(saved.id, fetcher)
    assertTrue(found.isPresent)
    assertEquals("perm.test", found.get().code)
  }
} 
