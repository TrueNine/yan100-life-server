package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.testtoolkit.testcontainers.IDatabasePostgresqlContainer
import net.yan100.life.infrastructure.jimmer.entities.Permission
import net.yan100.life.infrastructure.jimmer.entities.Role
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
class RoleRepoTest @Autowired constructor(
  val repo: IRoleRepo,
  val permRepo: IPermissionRepo,
) : IDatabasePostgresqlContainer {
  @Test
  fun `test create and find role with permissions`() {
    val perm = permRepo.save(Permission {
      id = 1L
      code = "perm.role"
      name = "角色权限"
      description = null
    })
    val role = repo.save(Role {
      id = 1L
      code = "role.test"
      name = "测试角色"
      description = null
      permissions = listOf(perm)
    })
    val fetcher = newFetcher(Role::class).by {
      allScalarFields()
      permissions { allScalarFields() }
    }
    val found = repo.findById(role.id, fetcher)
    assertTrue(found.isPresent)
    assertEquals("role.test", found.get().code)
    assertEquals(1, found.get().permissions.size)
    assertEquals("perm.role", found.get().permissions[0].code)
  }
} 
