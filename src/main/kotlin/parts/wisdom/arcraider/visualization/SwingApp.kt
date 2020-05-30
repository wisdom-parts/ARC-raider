package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.loadTaskFromFile
import java.awt.EventQueue
import java.io.File

private fun createAndShowGUI() {
    val task = loadTaskFromFile(
            File(object {}.javaClass.classLoader.getResource("extend_right.json")!!.toURI()))
    val frame = ARCWindow("ARCRaider", task)
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}