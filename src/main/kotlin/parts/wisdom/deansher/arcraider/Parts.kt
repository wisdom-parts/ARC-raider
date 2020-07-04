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

object ThePathFinder : Role<PathFinder>()
object PathFinder1 : Role<PathFinder>()
object PathFinder2 : Role<PathFinder>()
object ThePathTransformation : Role<PathTransformation>()

enum class Direction(val dx: Int, val dy: Int) {
    RIGHT(1, 0),
    DOWN_RIGHT(1, 1),
    DOWN(0, 1),
    DOWN_LEFT(-1, 1),
    LEFT(-1, 0),
    UP_LEFT(-1, -1);

    fun rotateClockwise45NDegrees(n: Int): Direction {
        val newOrdinal = (ordinal + n) % values().size
        return values()[newOrdinal]
    }
}

open class GridGenerator(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
    val backgroundColor by BackgroundColor

    // shapes listed bottom to top
    val shapes by TheShapes

    fun generate(): ArcGrid {
        val grid = ArcGrid(height, width, backgroundColor)
        for (shape in shapes) {
            shape.render(grid)
        }
        return grid
    }
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
    fun transform(generator: GridGenerator): GridGenerator
}

interface PathFinder {
    fun findPath(from: Mix): RolePath<*>
}

class PathFinderTransformation(vararg parts: Part) : Transformation, Mix(*parts) {
    val pathFinder by ThePathFinder
    val pathTransformation by ThePathTransformation

    override fun transform(generator: GridGenerator): GridGenerator =
        pathTransformation.transform(generator, pathFinder.findPath(generator))
}

class BottomShapeOfGenerator(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        require(from is GridGenerator)
        return TheShapes[0]
    }
}

class LengthPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return RolePath(Length)
    }
}

class TheColorPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return RolePath(TheColor)
    }
}

class StartXPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return Start + X
    }
}

class StartYPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return Start + Y
    }
}

class TheDirectionPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return RolePath(TheDirection)
    }
}

class ComposePathFinders(vararg parts: Part) : PathFinder, Mix(*parts) {
    val pathFinder1 by PathFinder1
    val pathFinder2 by PathFinder2

    @Suppress("UNCHECKED_CAST")
    override fun findPath(from: Mix): RolePath<*> {
        val path1 = pathFinder1.findPath(from) as RolePath<Mix>
        val mixAtPath1 = from[path1]
        check(mixAtPath1 != null)
        return path1 + pathFinder2.findPath(mixAtPath1)
    }
}

interface PathTransformation {
    fun transform(
        generator: GridGenerator,
        targetPath: RolePath<*>
    ): GridGenerator
}

/**
 * Inserts a constant `shape` at a constant `index` in a required `List<Shape>` at `targetPath`.
 * First, coerces `index` into the range `0 .. shapes.size`.
 */
class InsertConstantShape(vararg parts: Part) : PathTransformation, Mix(*parts) {
    val shape by TheShape
    val index by Index

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        generator: GridGenerator,
        targetPath: RolePath<*>
    ): GridGenerator = generator.mapAt(
        targetPath as RolePath<List<Shape>>
    ) { shapes ->
        val index = index.coerceIn(0..shapes.size)
        val prefix = shapes.slice(0 until index)
        val suffix = shapes.slice(index until shapes.size)
        prefix + listOf(shape) + suffix
    }
}

/**
 * Adds a constant `addend` to a required `Int` at `targetPath`.
 */
class AddConstant(vararg parts: Part) : PathTransformation, Mix(*parts) {
    val addend by Addend

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        generator: GridGenerator,
        targetPath: RolePath<*>
    ): GridGenerator = generator.mapAt(targetPath as RolePath<Int>) { it + addend }
}

/**
 * Replaces a required `Int` at `targetPath` with a constant `newInt`.
 */
class ReplaceIntWithConstant(vararg parts: Part) : PathTransformation, Mix(*parts) {
    val newInt by NewInt

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        generator: GridGenerator,
        targetPath: RolePath<*>
    ): GridGenerator = generator.mapAt(
        targetPath as RolePath<Int>
    ) { newInt }
}

/**
 * Replaces a required `ArcColor` at `targetPath` with a constant `newColor`.
 */
class ReplaceArcColorWithConstant(vararg parts: Part) : PathTransformation, Mix(*parts) {
    val newColor by NewColor

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        generator: GridGenerator,
        targetPath: RolePath<*>
    ): GridGenerator = generator.mapAt(
        targetPath as RolePath<ArcColor>
    ) { newColor }
}

class RotateDirectionClockwise45NDegrees(vararg parts: Part) : PathTransformation, Mix(*parts) {
    val steps by Steps

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        generator: GridGenerator,
        targetPath: RolePath<*>
    ): GridGenerator = generator.mapAt(
        targetPath as RolePath<Direction>
    ) { it.rotateClockwise45NDegrees(steps) }
}





