package parts.wisdom.utils

import me.joypri.to
import parts.wisdom.arcraider.Color
import parts.wisdom.arcraider.Direction
import parts.wisdom.arcraider.Length
import parts.wisdom.arcraider.TheColor
import parts.wisdom.arcraider.TheDirection
import parts.wisdom.arcraider.X
import parts.wisdom.arcraider.Y
import parts.wisdom.hdezninirola.lineworld.Line
import java.util.*


class RandomLineGenerator(seed: Long? = null, gridWidth: Int, gridHeight: Int) {

    private val rnd = seed?.let { Random(it) } ?: Random()

    val lineGeneratorSequence = sequence {
        while (true) {
            val direction = Direction.values()[(0..1).random()]
            val xOffset = if (direction == Direction.RIGHT) 1 else 0
            val yOffset = if (direction == Direction.DOWN) 1 else 0
            val x = (0 until gridWidth - xOffset).random()
            val y = (0 until gridHeight - yOffset).random()
            val length =
                    if (direction == Direction.RIGHT) (1..gridWidth - x).random()
                    else (1..gridHeight - y).random()

            yield(
                    Line(X to x,
                            Y to y,
                            TheColor to Color.values()[(Color.values().indices).random()],
                            TheDirection to direction,
                            Length to length
                    )
            )
        }
    }

    private fun IntRange.random() = rnd.nextInt((endInclusive + 1) - start) + start
}

fun main() {
    RandomLineGenerator(
            seed = null,
            gridWidth = 7,
            gridHeight = 7
    ).lineGeneratorSequence.take(20).toList().forEach {
        println("(${it.x}, ${it.y}) - Length ${it.length} - Direction ${it.direction} - Color ${it.color}")
    }
}
