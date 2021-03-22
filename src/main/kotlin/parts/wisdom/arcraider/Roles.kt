package parts.wisdom.arcraider

import me.joypri.ClassRole
import me.joypri.IntRole
import me.joypri.Role

object Width : IntRole()
object Height : IntRole()
object X : IntRole()
object Y : IntRole()
object Length : IntRole()
object Extension : IntRole()

object Shape : ClassRole<ArcMix>(ArcMix::class)
object Start : ClassRole<Coords>(Coords::class)
object TheColor : ClassRole<ArcColor>(ArcColor::class)
object TheCoordsDelta : ClassRole<CoordsDelta>(CoordsDelta::class)
object TheDirection : ClassRole<Direction>(Direction::class)
object TheGrid : ClassRole<Grid>(Grid::class)
object TheLine : ClassRole<Line>(Line::class)

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