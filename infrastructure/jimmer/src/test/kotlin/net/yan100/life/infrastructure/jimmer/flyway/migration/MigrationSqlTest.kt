package net.yan100.life.infrastructure.jimmer.flyway.migration

import jakarta.inject.Inject
import net.yan100.compose.testtoolkit.testcontainers.IDatabasePostgresqlContainer
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.init.ScriptUtils
import javax.sql.DataSource

@SpringBootTest
class MigrationSqlTest
@Inject
constructor(
  private val jdbcTemplate: JdbcTemplate,
  private val dataSource: DataSource,
) : IDatabasePostgresqlContainer {
  private val tables = listOf(
    "permission", "role", "role_group", "user_account",
    "role_permission", "role_group_role", "user_account_role_group",
    "post_content", "audit", "message"
  )

  @Test
  fun `V2001__init_rbac_tables sql 可重复执行且所有表均存在`() {
    val sqlResource = ClassPathResource("db/migration/V2001__init_rbac_tables.sql")
    // 再次执行 migration 脚本，校验幂等性
    ScriptUtils.executeSqlScript(dataSource.connection, sqlResource)
    // 检查所有表是否存在
    tables.forEach { table ->
      val count = jdbcTemplate.queryForObject(
        "select count(*) from information_schema.tables where table_name = ?",
        Int::class.java, table
      )
      assertTrue(count != null && count > 0, "表 $table 未创建")
    }
  }

  @Test
  fun `V2002__add_content_tables sql 可重复执行且所有表均存在`() {
    val sqlResource = ClassPathResource("db/migration/V2002__add_content_tables.sql")
    // 再次执行 migration 脚本，校验幂等性
    ScriptUtils.executeSqlScript(dataSource.connection, sqlResource)
    // 检查所有表是否存在
    listOf("post_content", "audit", "message").forEach { table ->
      val count = jdbcTemplate.queryForObject(
        "select count(*) from information_schema.tables where table_name = ?",
        Int::class.java, table
      )
      assertTrue(count != null && count > 0, "表 $table 未创建")
    }
  }
}
