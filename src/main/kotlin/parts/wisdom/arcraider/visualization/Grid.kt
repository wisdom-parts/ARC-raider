package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.SerializedGrid
import parts.wisdom.arcraider.ArcColor
import java.awt.Color

data class Square(val x: Int, val y: Int, val color: Int)

data class Grid(
    val widthSquares: Int,
    val heightSquares: Int,
    val squares: List<Square>
) {

    constructor(serializedGrid: SerializedGrid) :
            this(serializedGrid[0].size, serializedGrid.size, generateSquares(serializedGrid))
}

fun generateSquares(serializedGrid: SerializedGrid): List<Square> {
    val squares = mutableListOf<Square>()
    serializedGrid.forEachIndexed { row, colValues ->
        colValues.forEachIndexed { col, value ->
            if (value != 0) {
                squares.add(Square(col, row, value))
            }
        }
    }
    return squares
}

fun getColor(colorIndex: Int): Color =
    when (ArcColor.fromInt(colorIndex)) {
        ArcColor.BLACK -> Color.BLACK
        ArcColor.BLUE -> Color.BLUE
        ArcColor.RED -> Color.RED
        ArcColor.GREEN -> Color.GREEN
        ArcColor.YELLOW -> Color.YELLOW
        ArcColor.GRAY -> Color.GRAY
        ArcColor.FUCHSIA -> Color(0xC154C1)
        ArcColor.ORANGE -> Color.ORANGE
        ArcColor.TEAL -> Color(0x7FDBFF)
        ArcColor.CRIMSON -> Color(0x870C25)
    }
