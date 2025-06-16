package net.yan100.life.infrastructure.jimmer.autoconfig

import jakarta.annotation.PostConstruct
import net.yan100.compose.slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.context.annotation.DependsOn
import java.sql.DriverManager
import java.sql.SQLException

/**
 * 自动检查并创建 PostgreSQL 数据库（如不存在）
 * 加载时机：数据源加载后，flyway 之前
 */
@AutoConfiguration
@AutoConfigureBefore(FlywayAutoConfiguration::class)
@DependsOn("dataSource")
class DatabaseAutoCreator(
  @param:Value("\${spring.datasource.url}")
  private val url: String,
  @param:Value("\${spring.datasource.username}")
  private val username: String,
  @param:Value("\${spring.datasource.password}")
  private val password: String,
) {
  companion object {
    @JvmStatic
    private val log = slf4j<DatabaseAutoCreator>()
  }

  @PostConstruct
  fun checkAndCreateDatabase() {
    log.trace("checkAndCreateDatabase called, url: $url")
    if (!url.startsWith("jdbc:postgresql://")) {
      log.debug("Datasource url is not PostgreSQL, skip database auto creation. url: $url")
      return
    }
    val regex = Regex("jdbc:postgresql://([^:/]+)(?::(\\d+))?/([^?]+)")
    val match = regex.find(url)?.destructured
    if (match == null) {
      log.warn("Failed to parse database url: $url")
      return
    }
    val (host, port, dbName) = match
    log.trace("Parsed host: $host, port: $port, dbName: $dbName")
    val adminUrl = buildString {
      append("jdbc:postgresql://$host")
      if (port.isNotEmpty()) append(":$port")
      append("/postgres")
    }
    log.debug("Connecting to admin database with url: $adminUrl")
    try {
      DriverManager.getConnection(adminUrl, username, password).use { adminConn ->
        log.trace("Admin connection established successfully.")
        adminConn.createStatement().use { stmt ->
          val rs = stmt.executeQuery(
            """
                        SELECT 1 FROM pg_database WHERE datname = '$dbName'
                    """.trimIndent()
          )
          if (rs.next()) {
            log.debug("Database '$dbName' already exists, skip creation.")
            return
          }
          log.info("Database '$dbName' does not exist, creating...")
          stmt.executeUpdate("CREATE DATABASE \"$dbName\"")
          log.info("Database '$dbName' created successfully.")
        }
      }
    } catch (ex: SQLException) {
      log.warn("Failed to create database '$dbName': ${ex.message}")
      throw RuntimeException("Failed to auto create database: $dbName", ex)
    }
  }
} 
