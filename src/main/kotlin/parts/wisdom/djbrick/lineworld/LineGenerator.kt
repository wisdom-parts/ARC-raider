package parts.wisdom.djbrick.lineworld

import me.joypri.to
import parts.wisdom.arcraider.*
import java.util.*

public const val MAX_X = 20
public const val MAX_Y = 20

val lineGeneratorSequence = sequence {
    while (true) {
        val x = (0 until MAX_X - 1).random()
        val y = (0 until MAX_Y - 1).random()
        val start = Coords(X to x, Y to y)
        val direction = Direction.values()[(0..1).random()]
        val length = if (direction == Direction.RIGHT) (1..MAX_X - 1 - x).random() else (1..MAX_Y - 1 - y).random()

        yield(Line(
                Start to start,
                TheColor to Color.values()[(1 until Color.values().size).random()],
                TheDirection to direction,
                Length to length)
        )
    }
}
