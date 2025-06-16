package net.yan100.life.infrastructure.jimmer.autoconfig

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.*
import kotlin.test.assertFailsWith

class DatabaseAutoCreatorTest {
  private val username = "user"
  private val password = "pass"

  @BeforeEach
  fun setUp() {
    mockkStatic(DriverManager::class)
  }

  @AfterEach
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun `should skip if not postgresql url`() {
    val creator = DatabaseAutoCreator(
      url = "jdbc:mysql://localhost:5432/testdb",
      username = username,
      password = password
    )
    creator.checkAndCreateDatabase() // 不抛异常即通过
  }

  @Test
  fun `should skip if url cannot be parsed`() {
    val creator = DatabaseAutoCreator(
      url = "jdbc:postgresql://badurl",
      username = username,
      password = password
    )
    creator.checkAndCreateDatabase() // 不抛异常即通过
  }

  @Test
  fun `should skip if database already exists`() {
    val conn = mockk<Connection>(relaxed = true)
    val stmt = mockk<Statement>(relaxed = true)
    val rs = mockk<ResultSet>(relaxed = true)
    every { DriverManager.getConnection(any(), any(), any()) } returns conn
    every { conn.createStatement() } returns stmt
    every { stmt.executeQuery(any()) } returns rs
    every { rs.next() } returns true

    val creator = DatabaseAutoCreator(
      url = "jdbc:postgresql://localhost:5432/testdb",
      username = username,
      password = password
    )
    creator.checkAndCreateDatabase()

    verify(exactly = 0) { stmt.executeUpdate(any()) }
    verify { conn.close() }
  }

  @Test
  fun `should create database if not exists`() {
    val conn = mockk<Connection>(relaxed = true)
    val stmt = mockk<Statement>(relaxed = true)
    val rs = mockk<ResultSet>(relaxed = true)
    every { DriverManager.getConnection(any(), any(), any()) } returns conn
    every { conn.createStatement() } returns stmt
    every { stmt.executeQuery(any()) } returns rs
    every { rs.next() } returns false
    every { stmt.executeUpdate(any()) } returns 1

    val creator = DatabaseAutoCreator(
      url = "jdbc:postgresql://localhost:5432/testdb",
      username = username,
      password = password
    )
    creator.checkAndCreateDatabase()

    verify { stmt.executeUpdate(match { it.contains("CREATE DATABASE") }) }
    verify { conn.close() }
  }

  @Test
  fun `should throw if SQLException occurs`() {
    val conn = mockk<Connection>(relaxed = true)
    val stmt = mockk<Statement>(relaxed = true)
    every { DriverManager.getConnection(any(), any(), any()) } returns conn
    every { conn.createStatement() } returns stmt
    every { stmt.executeQuery(any()) } throws SQLException("fail!")

    val creator = DatabaseAutoCreator(
      url = "jdbc:postgresql://localhost:5432/testdb",
      username = username,
      password = password
    )
    assertFailsWith<RuntimeException> {
      creator.checkAndCreateDatabase()
    }
    verify { conn.close() }
  }
} 
