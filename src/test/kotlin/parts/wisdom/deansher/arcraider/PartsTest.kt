package parts.wisdom.deansher.arcraider

import me.joypri.to
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import parts.wisdom.arcraider.ArcColor

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PartsTest {
    @Test
    fun `GridGenerator with one Line renders properly`() {
        val genGridWithOneLine = GridGenerator(
            Width to 5,
            Height to 4,
            BackgroundColor to ArcColor.GRAY,
            TheShapes to listOf(
                Line(
                    TheColor to ArcColor.RED,
                    Start to Coords(X to 1, Y to 1),
                    TheDirection to Direction.DOWN_RIGHT,
                    Length to 2
                )
            )
        )
        assertEquals(
            ArcGrid.fromText(
                "aaaaa",
                "araaa",
                "aaraa",
                "aaaaa"
            ),
            genGridWithOneLine.generate()
        )
    }
}
