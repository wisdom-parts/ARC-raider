package parts.wisdom.arcraider.visualization

import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel
import kotlin.math.min

// When calculating the width / height of each grid pixel, offset it so
// we don't fill right to the edge of the screen.
private const val PANEL_INSET_PIXELS = 10

class GridPane(private var grid: Grid, private val isOutput: Boolean = false) : JPanel() {

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val graphics2d = g.create()

        // Draw the background
        graphics2d.color = Color.BLACK
        val (xPixels, yPixels) = gridSquaresToPixels(grid.widthSquares, grid.heightSquares)

        // For the output grids, we want them to be aligned on the right edge, so we offset their x
        // coordinate by their size.
        val xOffset = if (isOutput) width - xPixels - PANEL_INSET_PIXELS else 0
        graphics2d.fillRect(
                0 + xOffset,
                0,
                xPixels + PANEL_INSET_PIXELS,
                yPixels + PANEL_INSET_PIXELS
        )

        graphics2d.color = Color.WHITE
        // Draw the Empty Grid.
        for (vert in 0 until grid.heightSquares) {
            for (horiz in 0 until grid.widthSquares) {
                val (x, y) = gridSquaresToPixels(horiz, vert)
                graphics2d.drawRect(x + xOffset, y, pixelsPerSquare(), pixelsPerSquare())
            }
        }

        // Draw the active coordinates on the grid.
        grid.squares.forEach {
            graphics2d.color = getColor(it.color)
            val (x, y) = gridSquaresToPixels(it.x, it.y)
            graphics2d.fillRect(
                    (x + xOffset) + 1,
                    y + 1,
                    pixelsPerSquare() - 1,
                    pixelsPerSquare() - 1
            )
        }

        graphics2d.dispose()
    }

    private fun gridSquaresToPixels(xGridSquares: Int, yGridSquares: Int): Pair<Int, Int> {
        val xPixels = PANEL_INSET_PIXELS + xGridSquares * pixelsPerSquare()
        val yPixels = PANEL_INSET_PIXELS + yGridSquares * pixelsPerSquare()
        return Pair(xPixels, yPixels)
    }

    private fun pixelsPerSquare(): Int {
        return min(
                (width - PANEL_INSET_PIXELS * 2) / grid.widthSquares,
                (height - PANEL_INSET_PIXELS * 2) / grid.heightSquares
        )
    }
}