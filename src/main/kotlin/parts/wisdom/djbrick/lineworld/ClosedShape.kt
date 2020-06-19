package parts.wisdom.djbrick.lineworld

import me.joypri.Part
import parts.wisdom.arcraider.*
import parts.wisdom.arcraider.visualization.Grid
import parts.wisdom.arcraider.visualization.Square
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import me.joypri.to
import kotlin.collections.HashMap


open class ClosedShape(vararg parts : Part) : ComputedArcMix(*parts) {
    val start by theDot
    var holes : MutableList<Set<Dot>> = ArrayList()
    var border : Set<Dot> = HashSet()


    override fun invoke(grid: Grid): Grid {

        TODO("just draw the border")
    }

    override fun compute(dot: Dot, grid: Grid) {

        // Find the colored shape.
        border = findShape(start, grid.squares, Color.GREEN)

        // Subtract the shape from the total grid
        var resultingSquares = subtractFromGrid(border, grid.squares)

        // Find all the black shapes left
        var foundShape : Set<Dot> = findShape(squareToDot(resultingSquares[0]), resultingSquares, Color.BLACK)
        while(foundShape.size != 0) {
            // Detect if the black shape is inside the colored shape
            if (isEnclosed(foundShape, border)) {
                holes.add(foundShape)
            }

            resultingSquares = subtractFromGrid(foundShape, resultingSquares)
            foundShape = findShape(squareToDot(resultingSquares[0]), resultingSquares, Color.BLACK)
        }
    }


    fun findShape(dot : Dot, gridSquares: List<Square>, color : Color) : Set<Dot> {
        val toVisit : Queue<Dot> = LinkedList<Dot>()
        val visited : MutableSet<Dot> = HashSet<Dot>()

        toVisit.add(start)
        while (!toVisit.isEmpty()) {
            val dotOn = toVisit.poll()
            if (visited.contains(dotOn)) {
                continue
            }
            visited.add(dotOn)

            toVisit.addAll(findNeighbors(dotOn, gridSquares, color))
        }

        return visited
    }

    fun findNeighbors(dot : Dot, gridSquares : List<Square>, color : Color) : List<Dot> {
        val neighbors : MutableList<Dot> = ArrayList()
        for (square in gridSquares) {
            if ((abs(dot.start.x - square.x) <= 1) and (abs(dot.start.y - square.y) <= 1)) {

                    // TODO for now as long as the neighbor isn't black it counts
                    if ((color != Color.BLACK) and (square.color == 0 )) {
                        continue
                    }
                    neighbors.add(squareToDot(square))
            }
        }
        return neighbors
    }


    // TODO this is n^2 could be simpler with an actual 2d grid instead of a list
    fun subtractFromGrid(shape : Set<Dot>, gridSquares : List<Square>) : List<Square> {
        val mutableGridSquares = gridSquares.toMutableList()

        for (square in gridSquares) {
            for (dot in shape) {
                if ((square.x == dot.start.x) and (square.y == dot.start.y) ) {
                    mutableGridSquares.remove(square)
                }
            }
        }

        return gridSquares
    }

    fun squareToDot(square : Square) : Dot {
        val start = Coords(X to square.x, Y to square.y)
        // TODO integrate deans color mapping
        return Dot(Start to start, TheColor to Color.GREEN)
    }

    fun isEnclosed(innerShape : Set<Dot>, outerShape: Set<Dot>) : Boolean {
        // To be enclosed the shape has to be a rectangle and has to have perimeter border squares
        if (!isRectangle(innerShape)) {
            return false
        }

        val dimens = calculateRectangleDimens(innerShape)
        val perimeter = 2 * dimens.length + 2 * dimens.width

        return countBorderDots(innerShape, outerShape) == perimeter

    }

    fun isRectangle(shape : Set<Dot>) : Boolean {
        // A shape is a rectangle if it has a l * w that every pixel is within and exactly l* w pixels
        val dimens = calculateRectangleDimens(shape)

        if (shape.size != dimens.length * dimens.width) {
            return false
        }

        for (dot in shape) {
            if (!((dot.start.x <= dimens.mostX) and (dot.start.x >= dimens.leastX) and (dot.start.y <= dimens.mostY) and (dot.start.y >= dimens.leastY))){
                return false
            }
        }

        return true
    }

    data class RectangleDimens(val leastX : Int, val mostX: Int, val leastY : Int, val mostY: Int, val length : Int, val width : Int)

    fun calculateRectangleDimens(shape : Set<Dot>) : RectangleDimens {
        val dotOn = shape.first()

        val dotsToTheLeft = traverseRectangleDimension(dotOn, shape, Direction.LEFT)
        val dotsToTheRight = traverseRectangleDimension(dotOn, shape, Direction.RIGHT)
        val dotsUp = traverseRectangleDimension(dotOn, shape, Direction.UP)
        val dotsDown = traverseRectangleDimension(dotOn, shape, Direction.DOWN)

        val leastXCoord = dotOn.start.x - dotsToTheLeft
        val mostXCoord = dotOn.start.x + dotsToTheRight
        val leastYCoord = dotOn.start.y - dotsDown
        val mostYCoord = dotOn.start.y + dotsUp

        val length = 1 + dotsToTheLeft + dotsToTheRight
        val width = 1 + dotsUp + dotsDown

        return RectangleDimens(leastXCoord, mostXCoord, leastYCoord, mostYCoord, length, width)
    }

    fun traverseRectangleDimension(dot : Dot, shape : Set<Dot>, direction : Direction) : Int {
        var dotOn = dot
        var count = 0
        while(shape.contains(dotOn)) {
            var newCoords : Coords? = null
            if (direction == Direction.RIGHT) {
                var newCoords = Coords(X to dotOn.start.x + 1, Y to dotOn.start.y)
            } else if (direction == Direction.LEFT) {
                var newCoords = Coords(X to dotOn.start.x -1, Y to dotOn.start.y)
            } else if (direction == Direction.UP) {
                var newCoords = Coords(X to dotOn.start.x, Y to dotOn.start.y - 1)
            } else if (direction == Direction.DOWN) {
                var newCoords = Coords(X to dotOn.start.x, Y to dotOn.start.y + 1)
            }

            Dot(Start to newCoords!!, TheColor to dotOn.color)
            count++
        }
        return count
    }

    fun countBorderDots(shape : Set<Dot>, outerShape: Set<Dot>) : Int {
        // A Dot is a border dot if it has a least 1 of the 4 surrounding squares of a different color
        // TODO don't do this n^2
        var borderDots = 0
        for (dot in shape) {
            for (outerDot in outerShape) {
                if ((outerDot.start.x == dot.start.x + 1) and (outerDot.start.y == dot.start.y)) {
                    if (outerDot.color != Color.BLACK) {
                        borderDots++
                        continue
                    }
                }
                if ((outerDot.start.x == dot.start.x - 1) and (outerDot.start.y == dot.start.y)) {
                    if (outerDot.color != Color.BLACK) {
                        borderDots++
                        continue
                    }
                }
                if ((outerDot.start.x == dot.start.x) and (outerDot.start.y == dot.start.y + 1)) {
                    if (outerDot.color != Color.BLACK) {
                        borderDots++
                        continue
                    }
                }
                if ((outerDot.start.x == dot.start.x) and (outerDot.start.y == dot.start.y -1 )) {
                    if (outerDot.color != Color.BLACK) {
                        borderDots++
                        continue
                    }
                }
            }
        }

        return borderDots
    }



    enum class Direction { UP, DOWN, LEFT,  RIGHT }


}