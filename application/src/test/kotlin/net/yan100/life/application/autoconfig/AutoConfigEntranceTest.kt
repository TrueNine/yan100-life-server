package net.yan100.life.application.autoconfig

import net.yan100.compose.slf4j
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class AutoConfigEntranceTest {

  companion object {
    @JvmStatic
    private val log = slf4j<AutoConfigEntranceTest>()
  }

  @Test
  fun `正常 创建AutoConfigEntrance实例`() {
    log.trace("[正常 创建AutoConfigEntrance实例] starting test")
    // given & when
    val autoConfigEntrance = AutoConfigEntrance()

    // then
    assertNotNull(autoConfigEntrance)
  }
} 