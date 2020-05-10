package parts.wisdom.arcraider.visualization

import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

// When calculating the width / height of each grid pixel, offset it so
// we don't fill right to the edge of the screen.
private const val PANEL_INSET_PIXELS = 10

class GridPane(val grid: Grid) : JPanel() {

    private val xPixelsPerSquare  by lazy { (width - PANEL_INSET_PIXELS) /  grid.widthSquares }
    private val yPixelsPerSquare by lazy { (height - PANEL_INSET_PIXELS) / grid.heightSquares }
    private val xStartPixels by lazy { (width - xPixelsPerSquare * grid.widthSquares) / 2 }
    private val yStartPixels by lazy { (height - yPixelsPerSquare * grid.heightSquares) / 2 }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        background = Color.BLACK
        val graphics2d = g.create()
        graphics2d.color = Color.WHITE

        // Draw the Empty Grid.
        for (vert in 0 until grid.heightSquares) {
            for (horiz in 0 until grid.widthSquares) {
                val gridPixels = gridSquaresToPixels(horiz, vert)
                graphics2d.drawRect(gridPixels.first, gridPixels.second, xPixelsPerSquare, yPixelsPerSquare)
            }
        }

        // Draw the active coordinates on the grid.
        grid.coords.forEach {
            val gridPixels = gridSquaresToPixels(it.x, it.y)
            graphics2d.fillRect(gridPixels.first, gridPixels.second, xPixelsPerSquare, yPixelsPerSquare)
        }

        graphics2d.dispose()
    }

    fun gridSquaresToPixels(xGridSquares : Int, yGridSquares : Int) : Pair<Int, Int>{
        val xPixels = xStartPixels + xGridSquares * xPixelsPerSquare
        val yPixels = yStartPixels + yGridSquares * yPixelsPerSquare
        return Pair(xPixels, yPixels)

    }
}