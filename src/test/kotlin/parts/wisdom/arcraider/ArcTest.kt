package parts.wisdom.arcraider

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArcTest {
    @Test
    fun `load Task from file`() {
        val file = File(this::class.java.classLoader.getResource("extend_right.json").toURI())
        val task = loadTaskFromFile(file)
    }
}
