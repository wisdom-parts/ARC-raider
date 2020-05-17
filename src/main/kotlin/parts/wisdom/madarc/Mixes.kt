package parts.wisdom.madarc

import me.joypri.Mix
import me.joypri.Part
import parts.wisdom.arcraider.*
import java.lang.IllegalArgumentException

abstract class ArcMix(vararg parts: Part) : Mix(*parts) {
    val x by X
    val y by Y
    val color by TheColor
}

open class Rectangle(vararg parts: Part) : ArcMix(*parts) {
    val width by Width
    val height by Height
    val children = mutableListOf<ArcMix>()

    init {
        if (width <= 0 || height <= 0) throw IllegalArgumentException("Rectangle must have a positive width and length!")
    }

    operator fun component1() = width
    operator fun component2() = height
}

open class Line(vararg parts: Part) : ArcMix(*parts) {
    val direction by TheDirection
    val length by Length

    init {
        if (length <= 0) throw IllegalArgumentException("Line must have a positive length!")
    }
}

open class Pixel(vararg parts: Part) : ArcMix(*parts)