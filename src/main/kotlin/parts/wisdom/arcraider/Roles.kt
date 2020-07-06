package parts.wisdom.arcraider

import me.joypri.Role

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

    fun toChar(): Char =
        when(this) {
            BLACK -> '.'
            BLUE -> 'b'
            RED -> 'r'
            GREEN -> 'g'
            YELLOW -> 'y'
            GRAY -> 'a'
            FUCHSIA -> 'f'
            ORANGE -> 'o'
            TEAL -> 't'
            CRIMSON -> 'c'
        }

    companion object {
        private val intToColor = List(values().size) { values()[it] }

        fun fromInt(i: Int): ArcColor {
            require(i in intToColor.indices) { "no color for $i" }
            return intToColor[i]
        }

        private val charToColor = values().associateBy { it.toChar() }

        fun fromChar(ch: Char): ArcColor {
            val result = charToColor[ch]
            require (result != null) {
                "unknown color character code '$ch'"
            }
            return result
        }
    }
}

enum class Direction { DOWN, RIGHT }