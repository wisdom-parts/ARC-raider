package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.SerializedGrid

data class Coord( val x : Int, val y : Int)

open class Grid(
    val widthSquares: Int,
    val heightSquares: Int,
    val coords: List<Coord>) {

    constructor(serializedGrid : SerializedGrid) :
            this(serializedGrid[0].size, serializedGrid.size, generateCoords(serializedGrid))
}


fun generateCoords(serializedGrid: SerializedGrid): List<Coord> {

    val coords = mutableListOf<Coord>()
    serializedGrid.forEachIndexed { row, colValues ->
        colValues.forEachIndexed { col, value ->
            if (value != 0 ) {
                coords.add(Coord(row, col))
            }
        }
    }
    return coords
}
