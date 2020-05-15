package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.SerializedGrid
import java.awt.Color
import java.lang.IllegalStateException

data class Square(val x : Int, val y : Int, val color: Int)

data class Grid(
    val widthSquares: Int,
    val heightSquares: Int,
    val squares: List<Square>) {

    constructor(serializedGrid : SerializedGrid) :
            this(serializedGrid[0].size, serializedGrid.size, generateSquares(serializedGrid))
}

fun generateSquares(serializedGrid: SerializedGrid): List<Square> {
    val squares = mutableListOf<Square>()
    serializedGrid.forEachIndexed { row, colValues ->
        colValues.forEachIndexed { col, value ->
            if (value != 0 ) {
                squares.add(Square(col, row, value))
            }
        }
    }
    return squares
}

fun getColor(colorIndex: Int?): Color =  when(colorIndex) {
    0 -> Color.BLACK
    1 -> Color.BLUE
    2 -> Color.RED
    3 -> Color.GREEN
    4 -> Color.YELLOW
    5 -> Color.GRAY
    6 -> Color.MAGENTA
    7 -> Color.ORANGE
    8 -> Color.CYAN
    9 -> Color.PINK
    else -> throw IllegalStateException("Invalid Color")
}
