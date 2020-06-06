package parts.wisdom.arcraider

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RolesTest {
    @Test
    fun `bidirectional Color mapping`() {
        for (c in ArcColor.values()) {
            assertEquals(c, ArcColor.fromInt(c.ordinal))
        }
    }
}
