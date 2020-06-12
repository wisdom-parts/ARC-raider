package parts.wisdom.djbrick.lineworld

import me.joypri.to
import parts.wisdom.arcraider.*
import parts.wisdom.arcraider.visualization.Grid

class LineGame : Game {

    val line = lineGeneratorSequence.first()

    val myGrid = Grid(
            Height to MAX_Y,
            Width to MAX_X
    )

    var curGrid : Grid = getInputGrid()

    val outputHolder : Grid by lazy {
        var extendedLine =  Line(
                TheColor to line.color,
                TheDirection to line.direction,
                Start to line.start,
                Length to line.length + (0 until MAX_X).random())

        while (!validLine(extendedLine)) {
            extendedLine =  Line(
                    TheColor to line.color,
                    TheDirection to line.direction,
                    Start to line.start,
                    Length to line.length + (0 until MAX_X).random())
        }

        val gen = Generator(TheGrid to myGrid, Shape to extendedLine)
        gen(gen.makeGrid())
    }

    override fun run(gameProgress : GameProgress){
        // Main game loop
        while (true) {
            var done = false
            // Recognize the input line. Just cheat and use the input line with a random direction
            var gameLine = Line(
                    Start to line.start,
                    TheColor to line.color,
                    Length to line.length,
                    TheDirection to Direction.values()[(0..1).random()]
            )

            // While not done apply transformations (extend by 1 in direction)
            while (true) {
                // Evaluate if were correct or to the edge of grid if either finish if not make another move
                if (curGrid == getOutputGrid()) {
                    // We won!
                    done = true
                    break
                }

                Thread.sleep(500)
                gameLine = Line(
                        TheColor to gameLine.color,
                        TheDirection to gameLine.direction,
                        Start to gameLine.start,
                        Length to gameLine.length + 1
                )

                // Make the Grid
                val gen = Generator(TheGrid to myGrid, Shape to gameLine)
                curGrid  = gen(gen.makeGrid())

                // Report our move
                gameProgress.report()

                if (!validLine(gameLine)) {
                    // We lost try again.
                    break
                }
            }

            if (done) {
                break
            }
        }
    }

    override fun getInputGrid(): Grid {
        val gen = Generator(TheGrid to myGrid, Shape to line )
        return gen(gen.makeGrid())
    }

    override fun getOutputGrid(): Grid {
       return outputHolder
    }

    override fun getCurrentGrid(): Grid {
        return curGrid
    }

    fun validLine(line : Line) : Boolean {
        if (line.direction == Direction.RIGHT) {
            if (line.start.x + line.length > myGrid.width - 1) {
                return false
            }
        } else {
            if (line.start.y + line.length > myGrid.height - 1) {
                return false
            }
        }
        return true
    }
}