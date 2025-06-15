package net.yan100.life.domain.content

import net.yan100.life.domain.enums.PostContentStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PostContentStatusTest {
    @Test
    fun `test enum values and get`() {
        val all = PostContentStatus.entries
        assertTrue(all.isNotEmpty())
        for (e in all) {
            assertEquals(e, PostContentStatus[e.value])
        }
        assertNull(PostContentStatus[999999])
    }
} 
