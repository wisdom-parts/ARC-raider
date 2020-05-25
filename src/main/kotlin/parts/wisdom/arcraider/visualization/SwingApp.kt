package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.loadTaskFromFile
import java.awt.EventQueue
import java.io.File

private const val WINDOW_HEIGHT_PIXEL = 600
private const val WINDOW_WIDTH_PIXEL = 800

private fun createAndShowGUI() {
    val task = loadTaskFromFile(
            File(object {}.javaClass.classLoader.getResource("extend_right.json")!!.toURI()))
    val frame = ARCWindow(WINDOW_WIDTH_PIXEL, WINDOW_HEIGHT_PIXEL, "ARCRaider", task)
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}