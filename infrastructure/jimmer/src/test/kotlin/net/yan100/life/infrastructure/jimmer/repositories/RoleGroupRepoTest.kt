package net.yan100.life.infrastructure.jimmer.repositories

import net.yan100.compose.testtoolkit.testcontainers.IDatabasePostgresqlContainer
import net.yan100.life.infrastructure.jimmer.entities.Role
import net.yan100.life.infrastructure.jimmer.entities.RoleGroup
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
class RoleGroupRepoTest @Autowired constructor(
  val repo: IRoleGroupRepo,
  val roleRepo: IRoleRepo,
) : IDatabasePostgresqlContainer {
  @Test
  fun `test create and find role group with roles`() {
    val role = roleRepo.save(Role {
      id = 1L
      code = "role.group"
      name = "组内角色"
      description = null
      permissions = emptyList()
    })
    val group = repo.save(RoleGroup {
      id = 1L
      code = "group.test"
      name = "测试组"
      description = null
      roles = listOf(role)
    })
    val fetcher = newFetcher(RoleGroup::class).by {
      allScalarFields()
      roles { allScalarFields() }
    }
    val found = repo.findById(group.id, fetcher)
    assertTrue(found.isPresent)
    assertEquals("group.test", found.get().code)
    assertEquals(1, found.get().roles.size)
    assertEquals("role.group", found.get().roles[0].code)
  }
} 
