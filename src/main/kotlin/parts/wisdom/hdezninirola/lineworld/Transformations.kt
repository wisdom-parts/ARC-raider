package parts.wisdom.hdezninirola.lineworld

import me.joypri.to
import parts.wisdom.arcraider.*

interface Transformation<in T, out R> {
    fun transform(input: T): R
}

open class LineExtensionTransformation : Transformation<Line, Line> {
    override fun transform(input: Line): Line {
        return Line(
                X to input.x,
                Y to input.y,
                TheColor to input.color,
                TheDirection to input.direction,
                Length to input.length + 1
        )
    }
}

open class LineRotationTransformation : Transformation<Line, Line> {
    override fun transform(input: Line): Line {
        return Line(
                X to input.x,
                Y to input.y,
                TheColor to input.color,
                Length to input.length,
                TheDirection to Direction.values()[(input.direction.ordinal + 1) % Direction.values().size]
        )
    }
}

open class ShapeHorizontalTranslationTransformation<T : ArcMix>: Transformation<T, T> {
    override fun transform(input: T): T {
        // Not sure how this would work, since it needs to return a clone of T with
        // just X modified. I think Remix is the way to go for transformations, but
        // I'll do that next.
        return input
    }
}