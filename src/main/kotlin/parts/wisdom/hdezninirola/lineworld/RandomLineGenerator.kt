package parts.wisdom.hdezninirola.lineworld

import me.joypri.to
import parts.wisdom.arcraider.*
import java.util.*


private const val MAX_X = 6
private const val MAX_Y = 6

val lineGeneratorSequence = sequence {
    while (true) {
        val x = (0 until MAX_X).random()
        val y = (0 until MAX_Y).random()
        val direction = Direction.values()[(0..1).random()]
        val length = if (direction == Direction.DOWN) (1..MAX_X - x).random() else (1..MAX_Y - y).random()

        yield(Line(X to x,
                Y to y,
                TheColor to Color.values()[(Color.values().indices).random()],
                TheDirection to direction,
                Length to length)
        )
    }
}

fun IntRange.random() =
        Random().nextInt((endInclusive + 1) - start) + start

fun main() {
    lineGeneratorSequence.take(20).toList().forEach {
        println("(${it.x}, ${it.y}) - Length ${it.length} - Direction ${it.direction} - Color ${it.color}")
    }
}