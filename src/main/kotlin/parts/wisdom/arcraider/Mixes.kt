package parts.wisdom.arcraider

import me.joypri.Mix
import me.joypri.Part
import parts.wisdom.arcraider.visualization.Square
import parts.wisdom.arcraider.visualization.Grid as VisualGrid
import java.lang.IllegalArgumentException

abstract class ArcMix(vararg parts: Part) : Mix(*parts) {

    // Draws the Mix onto a Grid
    abstract operator fun invoke(grid: VisualGrid): VisualGrid
}

abstract class ComputedArcMix(vararg parts: Part) : ArcMix(*parts) {

    // Computes the Mix from a Grid and a starting pixel
    abstract  fun compute(dot : Dot, grid: VisualGrid)
}

open class Grid(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
}

open class Coords(vararg parts: Part) : Mix(*parts) {
    val x by X
    val y by Y

    operator fun component1() = x
    operator fun component2() = y
}

open class CoordsDelta(vararg parts: Part) : Mix(*parts) {
    val xOffset by X
    val yOffset by Y

    operator fun component1() = xOffset
    operator fun component2() = yOffset
}

open class Dot(vararg parts: Part) : ArcMix(*parts) {
    val start by Start
    val color by TheColor

    override fun invoke(grid: VisualGrid): VisualGrid {
        val newSquares = grid.squares.toMutableList()
        newSquares.add(Square(start.x, start.y, color.ordinal))
        return grid.copy(squares = newSquares)
    }
}

open class Line(vararg parts: Part) : ArcMix(*parts) {
    val start by Start
    val color by TheColor
    val direction by TheDirection
    val length by Length

    init {
        if (length <= 0) throw IllegalArgumentException("Line must have a positive length!")
    }

    override fun invoke(grid: VisualGrid): VisualGrid {
        val newSquares = grid.squares.toMutableList()
        val (xStart, yStart) = start
        for (delta in 0.until(length)) {
            when (direction) {
                Direction.RIGHT -> newSquares.add(Square(xStart + delta, yStart, color.ordinal))
                Direction.DOWN -> newSquares.add(Square(xStart,yStart + delta, color.ordinal))
            }
        }
        return grid.copy(squares = newSquares)
    }
}