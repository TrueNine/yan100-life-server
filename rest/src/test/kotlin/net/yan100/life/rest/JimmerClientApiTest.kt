package net.yan100.life.rest

import jakarta.inject.Inject
import net.yan100.compose.testtoolkit.testcontainers.IDatabasePostgresqlContainer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(classes = [TestEntrance::class])
@AutoConfigureMockMvc
class JimmerClientApiTest @Inject constructor(
  private val mockMvc: MockMvc,
) : IDatabasePostgresqlContainer {
  @Test
  fun testTsZipShouldReturn200() {
    mockMvc.get("/ts.zip")
      .andExpect { status { isOk() } }
  }
} 
