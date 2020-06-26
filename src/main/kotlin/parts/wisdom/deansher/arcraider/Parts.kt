@file:Suppress("PublicApiImplicitType")

package parts.wisdom.deansher.arcraider

import me.joypri.*
import parts.wisdom.arcraider.ArcColor

object Width : Role<Int>()
object Height : Role<Int>()
object TheShape : Role<Shape>()
object TheShapes : Role<List<Shape>>()
object Index : Role<Int>()
object X : Role<Int>()
object Y : Role<Int>()
object Length : Role<Int>()
object Addend : Role<Int>()
object NewInt : Role<Int>()
object Steps : Role<Int>()

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
    UP_LEFT(-1, -1);

    fun rotate(steps: Int): Direction {
        val newOrdinal = (ordinal + steps) % values().size
        return values()[newOrdinal]
    }
}

open class GridGenerator(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
    val backgroundColor by BackgroundColor
    val shapes by TheShapes
}

interface Shape {
    /**
     * Renders this shape into `grid`.
     */
    fun render(grid: ArcGrid)
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
    fun transform(
        generator: GridGenerator,
        path1: RolePath,
        path2: RolePath
    ): GridGenerator
}

class InsertConstantShape(vararg parts: Part) : Transformation, Mix(*parts) {
    val shape by TheShape
    val index by Index

    override fun transform(
        generator: GridGenerator,
        path1: RolePath,
        path2: RolePath
    ): GridGenerator = generator.mapAt<GridGenerator, List<Shape>> {shapes ->
        val index = index.coerceIn(0 .. shapes.size)
        val prefix = shapes.slice(0 until index)
        val suffix = shapes.slice(index until shapes.size)
        prefix + listOf(shape) + suffix
    }
}

class AddConstant(vararg parts: Part) : Transformation, Mix(*parts) {
    val addend by Addend

    override fun transform(
        generator: GridGenerator,
        path1: RolePath,
        path2: RolePath
    ): GridGenerator = generator.mapAt<GridGenerator, Int>(path1) { it + addend }
}

class ReplaceIntWithConstant(vararg parts: Part) : Transformation, Mix(*parts) {
    val newInt by NewInt

    override fun transform(
        generator: GridGenerator,
        path1: RolePath,
        path2: RolePath
    ): GridGenerator = generator.mapAt<GridGenerator, Int>(path1) { newInt }
}

class ReplaceColorWithConstant(vararg parts: Part) : Transformation, Mix(*parts) {
    val newColor by NewColor
    override fun transform(
        generator: GridGenerator,
        path1: RolePath,
        path2: RolePath
    ): GridGenerator = generator.mapAt<GridGenerator, ArcColor> { newColor }
}

class RotateDirection(vararg parts: Part) : Transformation, Mix(*parts) {
    val steps by Steps
    override fun transform(
        generator: GridGenerator,
        path1: RolePath,
        path2: RolePath
    ): GridGenerator = generator.mapAt<GridGenerator, Direction> { it.rotate(steps) }
}





