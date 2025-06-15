package net.yan100.life.domain.content

import net.yan100.life.domain.enums.PostMessageType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PostMessageTypeTest {
    @Test
    fun `test enum values and get`() {
        val all = PostMessageType.entries
        assertTrue(all.isNotEmpty())
        for (e in all) {
            assertEquals(e, PostMessageType[e.value])
        }
        assertNull(PostMessageType["not-exist"])
    }
} 
