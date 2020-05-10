package parts.wisdom.arcraider

import me.joypri.Mix
import me.joypri.Part
import java.lang.IllegalArgumentException

abstract class ArcMix(vararg parts: Part) : Mix(*parts) {

    abstract operator fun invoke(grid: DavidsGrid): DavidsGrid
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

open class Dot(vararg parts: Part) : ArcMix(*parts) {
    val start by Start
    val color by TheColor

    override fun invoke(grid: DavidsGrid): DavidsGrid {
        return grid.also {
            it[start.y][start.x] = color
        }
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

    override fun invoke(grid: DavidsGrid): DavidsGrid {
        val (xStart, yStart) = start
        for (delta in 0.until(length)) {
            when (direction) {
                Direction.RIGHT -> grid[yStart][xStart + delta] = color
                Direction.UP -> grid[yStart - delta][xStart] = color
            }
        }
        return grid
    }
}