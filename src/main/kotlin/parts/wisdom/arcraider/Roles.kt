package parts.wisdom.arcraider

import me.joypri.Role
import java.lang.IllegalArgumentException

object Width : Role<Int>()
object Height : Role<Int>()
object X : Role<Int>()
object Y : Role<Int>()
object Length : Role<Int>()
object Extension : Role<Int>()

object Shape : Role<ArcMix>()
object Start : Role<Coords>()
object TheColor : Role<ArcColor>()
object TheCoordsDelta : Role<CoordsDelta>()
object TheDirection : Role<Direction>()
object TheGrid : Role<Grid>()
object TheLine : Role<Line>()

enum class ArcColor {
    BLACK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    GRAY,
    FUCHSIA,
    ORANGE,
    TEAL,
    CRIMSON;

    companion object {
        private val intToColor = List(values().size) { values()[it] }

        fun fromInt(i: Int): ArcColor {
            require(i in intToColor.indices) { "no color for $i" }
            return intToColor[i]
        }

        fun fromChar(ch: Char): ArcColor =
            when(ch) {
                '.' -> BLACK
                'b' -> BLUE
                'r' -> RED
                'g' -> GREEN
                'y' -> YELLOW
                'a' -> GRAY
                'f' -> FUCHSIA
                'o' -> ORANGE
                't' -> TEAL
                'c' -> CRIMSON
                else -> throw IllegalArgumentException("Unknown character code `$ch`")
            }
    }
}

enum class Direction { DOWN, RIGHT }