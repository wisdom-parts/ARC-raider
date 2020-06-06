package parts.wisdom.arcraider

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArcTest {
    @Test
    fun `load Task from file`() {
        val resource =
            this::class.java.classLoader.getResource("extend_right.json")
                ?: fail("missing test resource")
        val task = loadTaskFromFile(File(resource.toURI()))
        for (pair in task.test + task.train) {
            assertEquals(pair.input.size, 4)
            assertEquals(pair.output.size, 4)
            for (row in 0..3) {
                if (row == 1) {
                    assertEquals(pair.input.get(row), listOf(0, 1, 0, 0))
                    assertEquals(pair.output.get(row), listOf(0, 1, 1, 0))
                } else {
                    for (grid in listOf(pair.input, pair.output)) {
                        assertEquals(grid.get(row), listOf(0, 0, 0, 0))
                    }
                }
            }
        }
    }
}
