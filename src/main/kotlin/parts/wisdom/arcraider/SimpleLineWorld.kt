package parts.wisdom.arcraider

import me.joypri.Part
import me.joypri.to

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

    fun makeGrid(): DavidsGrid {
        return DavidsGrid(grid.width) {
            1.rangeTo(grid.height).map { Color.NONE }.toTypedArray()
        }
    }

    override fun invoke(grid: DavidsGrid): DavidsGrid {
        return line(grid)
    }
}

fun doStuff() {
    val gen = Generator(TheGrid to myGrid, TheLine to myLine)
    gen(gen.makeGrid())
}