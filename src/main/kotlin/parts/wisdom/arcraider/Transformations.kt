package parts.wisdom.arcraider

import me.joypri.Part
import me.joypri.to
import parts.wisdom.arcraider.visualization.Grid as VisualGrid

open class ExtensionLineTransformation(vararg parts: Part) : ArcMix(*parts) {
    val line by TheLine
    val extension by Extension

    override fun invoke(grid: VisualGrid): VisualGrid {
        val transformedLine = Line(
                TheColor to line.color,
                TheDirection to line.direction,
                Start to line.start,
                Length to line.length + extension
        )
        return transformedLine.invoke(grid)
    }
}

open class MovementLineTransformation(vararg parts: Part) : ArcMix(*parts) {
    val line by TheLine
    val coordsDelta by TheCoordsDelta

    override fun invoke(grid: VisualGrid): VisualGrid {
        val transformedLine = Line(
                TheColor to line.color,
                TheDirection to line.direction,
                Start to Coords(
                        X to line.start.x + coordsDelta.xOffset,
                        Y to line.start.y + coordsDelta.yOffset
                ),
                Length to line.length
        )
        return transformedLine.invoke(grid)
    }
}