package parts.wisdom.arcraider

import me.joypri.Part
import me.joypri.to
import parts.wisdom.arcraider.visualization.Grid as VisualGrid

/**
 *
 * This file is just an example of how to use our building blocks.
 */


val myGrid = Grid(
        Height to 5,
        Width to 3
)

val myLine = Line(
        TheColor to Color.RED,
        TheDirection to Direction.RIGHT,
        Start to Coords(X to 2, Y to 3),
        Length to 1
)

open class Generator(vararg parts: Part) : ArcMix(*parts) {
    val grid by TheGrid
    val line by TheLine

    fun makeGrid(): VisualGrid {
        return VisualGrid(grid.width, grid.height, emptyList())
    }

    override fun invoke(grid: VisualGrid): VisualGrid {
        return line(grid)
    }
}

fun doStuff() {
    val gen = Generator(TheGrid to myGrid, TheLine to myLine)
    gen(gen.makeGrid())
}