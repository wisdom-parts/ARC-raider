package parts.wisdom.deansher.arcraider

import me.joypri.Mix
import me.joypri.Part
import me.joypri.Role

object Width : Role<Int>()
object Height : Role<Int>()
object TheShape : Role<Shape>()
object X : Role<Int>()
object Y : Role<Int>()
object Length : Role<Int>()

object Start : Role<Coords>()
object TheColor : Role<ArcColor>()
object NewColor : Role<ArcColor>()
object BackgroundColor : Role<ArcColor>()
object TheDirection : Role<Direction>()

enum class Direction(val dx: Int, val dy: Int) {
    RIGHT(1, 0),
    DOWN_RIGHT(1, 1),
    DOWN(0, 1),
    DOWN_LEFT(-1, 1),
    LEFT(-1, 0),
    UP_LEFT(-1, -1)
}

open class GridGenerator(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
    val backgroundColor by BackgroundColor
    val shape by TheShape
}

interface Shape {
    /**
     * Renders this shape into `grid`.
     */
    fun render(grid: ArcGrid);
}

class Line(vararg parts: Part) : Shape, Mix(*parts) {
    val color by TheColor
    val start by Start
    val direction by TheDirection
    val length by Length

    override fun render(grid: ArcGrid) {
        for (i in 0 until length) {
            val x = start.x + i * direction.dx
            val y = start.y + i * direction.dy
            if (x in 0 until grid.width &&
                y in 0 until grid.height
            ) {
                grid[x, y] = color
            }
        }
    }
}

class Coords(vararg parts: Part) : Mix(*parts) {
    val x by X
    val y by Y
}

interface Transformation {
    fun transform(generator: GridGenerator, at: Any): GridGenerator;
}

class Lengthen(vararg parts: Part): Transformation, Mix(*parts) {
    override fun transform(
        generator: GridGenerator,
        at: Any
    ): GridGenerator {
        val line = at as Line
        TODO()
    }
}

class Shorten(vararg parts: Part): Transformation, Mix(*parts) {
    override fun transform(
        generator: GridGenerator,
        at: Any
    ): GridGenerator {
        val line = at as Line
        TODO()
    }
}

class ChangeBackgroundColor(vararg parts: Part): Transformation, Mix(*parts) {
    val newColor by NewColor

    override fun transform(
        generator: GridGenerator,
        at: Any
    ): GridGenerator {
        TODO()
    }
}




