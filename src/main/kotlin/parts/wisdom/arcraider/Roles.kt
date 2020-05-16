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
object TheColor : Role<Color>()
object TheCoordsDelta : Role<CoordsDelta>()
object TheDirection : Role<Direction>()
object TheGrid : Role<Grid>()
object TheLine : Role<Line>()

enum class Color {
    BLACK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    GRAY,
    MAGENTA,
    ORANGE,
    CYAN,
    PINK
}

enum class Direction { UP, RIGHT }