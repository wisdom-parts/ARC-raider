package parts.wisdom.arcraider

import me.joypri.Part
import me.joypri.to
import parts.wisdom.arcraider.visualization.Grid as VisualGrid

/**
 *
 * This file is just an example of how to use our building blocks.
 */


val myGrid = Grid(
        Height to 6,
        Width to 6
)

val myLine = Line(
        TheColor to Color.RED,
        TheDirection to Direction.RIGHT,
        Start to Coords(X to 2, Y to 3),
        Length to 1
)

val myTransformation = ExtensionLineTransformation(
        TheLine to myLine,
        Extension to 1
)

open class Generator(vararg parts: Part) : ArcMix(*parts) {
    val grid by TheGrid
    val shape by Shape

    fun makeGrid(): VisualGrid {
        return VisualGrid(grid.width, grid.height, emptyList())
    }

    override fun invoke(grid: VisualGrid): VisualGrid {
        return shape(grid)
    }
}

fun main() {
    val gen = Generator(TheGrid to myGrid, Shape to myLine)
    val grid = gen(gen.makeGrid())

    val gen2 = Generator(TheGrid to myGrid, Shape to myTransformation)
    val grid2 = gen2(gen2.makeGrid())

    print(grid)
    println("-----")
    print(grid2)

}
