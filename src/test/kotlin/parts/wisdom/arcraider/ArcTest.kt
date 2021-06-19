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
        val json =
            this::class.java.classLoader.getResource("extend_right.json")
                ?: fail("missing test resource")
        val task = loadTaskFromFile(File(json.toURI()))
        for (pair in task.train + task.test) {
            assertEquals(pair.input.width, 5)
            assertEquals(pair.input.height, 4)
            assertEquals(pair.output.width, 5)
            assertEquals(pair.output.height, 4)
        }
        assertEquals(task.train[0].input[0, 0], ArcColor.BLACK)
        assertEquals(task.train[0].input[1, 1], ArcColor.BLUE)
        assertEquals(task.train[0].output[2, 1], ArcColor.BLUE)
        assertEquals(task.test[0].input[0, 1], ArcColor.BLUE)
    }
}
