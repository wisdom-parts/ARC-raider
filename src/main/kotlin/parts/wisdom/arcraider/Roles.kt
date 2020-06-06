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
    FUSCHIA,
    ORANGE,
    TEAL,
    CRIMSON;

    companion object {
        private val intToColor = MutableList(values().size) { BLACK }.apply {
            for (c in values()) {
                set(c.ordinal, c)
            }
        }
        fun fromInt(i: Int): ArcColor {
            require(i in 0 until intToColor.size) { "no color for $i" }
            return intToColor[i]
        }
    }
}

enum class Direction { DOWN, RIGHT }