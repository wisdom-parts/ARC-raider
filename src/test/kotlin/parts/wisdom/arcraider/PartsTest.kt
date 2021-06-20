package parts.wisdom.arcraider

import me.joypri.of
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PartsTest {
    @Test
    fun `GridGenerator with one Line renders properly`() {
        val genGridWithOneLine = GridGenerator(
            Width of 5,
            Height of 4,
            BackgroundColor of ArcColor.GRAY,
            TheShapes of listOf(
                Line(
                    TheColor of ArcColor.RED,
                    Start of Coords(X of 1, Y of 1),
                    TheDirection of Direction.DOWN_RIGHT,
                    Length of 2
                )
            )
        )
        assertEquals(
            """ |aaaaa
                |araaa
                |aaraa
                |aaaaa""".trimMargin(),
            genGridWithOneLine.generate().toString()
        )
    }
}
