package parts.wisdom.deansher.arcraider

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import parts.wisdom.arcraider.ArcColor
import parts.wisdom.arcraider.loadTaskFromFile
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArcTest {
    @Test
    fun `load serialized Task from file and convert to ArcTask`() {
        val resource =
            this::class.java.classLoader.getResource("extend_right.json")
                ?: Assertions.fail("missing test resource")
        val task = ArcTask.fromSerialized(loadTaskFromFile(File(resource.toURI())))

        val color1 = ArcColor.fromInt(1)

        val expectedInput = ArcGrid(4, 4, ArcColor.BLACK)
        expectedInput[1, 1] = color1

        val expectedOutput = ArcGrid(4, 4, ArcColor.BLACK)
        expectedOutput[1, 1] = color1
        expectedOutput[2, 1] = color1

        for (pair in task.test + task.train) {
            assertEquals(expectedInput, pair.input)
            assertEquals(expectedOutput, pair.output)
        }
    }
}
