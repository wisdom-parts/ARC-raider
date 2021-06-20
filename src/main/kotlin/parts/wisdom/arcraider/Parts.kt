@file:Suppress("PublicApiImplicitType", "MemberVisibilityCanBePrivate", "unused")

package parts.wisdom.arcraider

import me.joypri.*
import java.lang.IllegalArgumentException

object Width : IntRole()
object Height : IntRole()
object X : IntRole()
object Y : IntRole()
object Length : IntRole()
object Extension : IntRole()

object TheCoordsDelta : ClassRole<CoordsDelta>(CoordsDelta::class)
object DX : IntRole()
object DY : IntRole()

object TheGrid : ClassRole<GridDimensions>(GridDimensions::class)
object TheLine : ClassRole<Line>(Line::class)

open class ShapeRole : ClassRole<Shape>(Shape::class)
open class ArcColorRole : ClassRole<ArcColor>(ArcColor::class)
open class PathFinderRole : ClassRole<PathFinder>(PathFinder::class)

object NewGenerator : ClassRole<GridGenerator>(GridGenerator::class)
object BackgroundColor : ArcColorRole()
object TheShapes : ListRole<Shape>(Shape::class)
object Index : IntRole()

object TheShape : ShapeRole()
object Addend : IntRole()
object NewInt : IntRole()
object Steps : IntRole()

object Start : ClassRole<Coords>(Coords::class)
object TheColor : ArcColorRole()
object NewColor : ArcColorRole()
object TheDirection : ClassRole<Direction>(Direction::class)

object ThePathFinder : PathFinderRole()
object PathFinder1 : PathFinderRole()
object PathFinder2 : PathFinderRole()
object ThePathTransformation : ClassRole<PathTransformation>(PathTransformation::class)

open class GridGenerator(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
    val backgroundColor by BackgroundColor

    // shapes listed bottom to top
    val shapes by TheShapes

    fun generate(): ArcGrid {
        val grid = ArcGrid(width, height, backgroundColor)
        for (shape in shapes) {
            shape.draw(grid)
        }
        return grid
    }
}

interface Shape {
    /**
     * Renders this shape into `grid`.
     */
    fun draw(grid: ArcGrid)
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

class TheShapesZeroPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return TheShapes[0]
    }
}

class LengthPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return Length.toPath()
    }
}

class TheColorPath(vararg parts: Part) : PathFinder, Mix(*parts) {
    override fun findPath(from: Mix): RolePath<*> {
        return TheColor.toPath()
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
        return TheDirection.toPath()
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
 * Replaces the generator entirely.
 */
class ReplaceGeneratorWithConstant(vararg parts: Part) : Transformation, Mix(*parts) {
    val newGenerator by NewGenerator

    @Suppress("UNCHECKED_CAST")
    override fun transform(
        generator: GridGenerator
    ): GridGenerator = newGenerator
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

abstract class GridDrawingMix(vararg parts: Part) : Mix(*parts) {

    abstract fun draw(grid: ArcGrid)
}

open class GridDimensions(vararg parts: Part) : Mix(*parts) {
    val width by Width
    val height by Height
}

open class Coords(vararg parts: Part) : Mix(*parts) {
    val x by X
    val y by Y

    operator fun component1() = x
    operator fun component2() = y

    operator fun plus(offset: Offset) =
        Coords(X of x + offset.dx, Y of y + offset.dy)
}

operator fun ArcGrid.set(coords: Coords, color: ArcColor) {
    this[coords.x, coords.y] = color
}

open class CoordsDelta(vararg parts: Part) : Mix(*parts) {
    val dx by DX
    val dy by DY

    operator fun component1() = dx
    operator fun component2() = dy
}

operator fun Coords.plus(delta: CoordsDelta): Coords =
    Coords(
        X of x + delta.dx,
        Y of y + delta.dy
    )

open class Dot(vararg parts: Part) : GridDrawingMix(*parts) {
    val start by Start
    val color by TheColor

    override fun draw(grid: ArcGrid) {
        grid[start] = color
    }
}

open class Line(vararg parts: Part) : GridDrawingMix(*parts), Shape {
    val start by Start
    val color by TheColor
    val direction by TheDirection
    val length by Length

    init {
        if (length <= 0) throw IllegalArgumentException("Line must have a positive length!")
    }

    override fun draw(grid: ArcGrid) {
        for (delta in 0.until(length)) {
            grid[start + direction * delta] = color
        }
    }
}

abstract class LineTransformation(vararg parts: Part) : Mix(*parts) {

    abstract fun transform(line: Line): Line
}

open class ExtendLine(vararg parts: Part) : LineTransformation(*parts) {
    val extension by Extension

    override fun transform(line: Line): Line {
        return Line(
            TheColor of line.color,
            TheDirection of line.direction,
            Start of line.start,
            Length of line.length + extension
        )
    }
}

open class MoveLine(vararg parts: Part) : LineTransformation(*parts) {
    val coordsDelta by TheCoordsDelta

    override fun transform(line: Line): Line {
        return Line(
            TheColor of line.color,
            TheDirection of line.direction,
            Start of Coords(
                X of line.start.x + coordsDelta.dx,
                Y of line.start.y + coordsDelta.dy
            ),
            Length of line.length
        )
    }
}




