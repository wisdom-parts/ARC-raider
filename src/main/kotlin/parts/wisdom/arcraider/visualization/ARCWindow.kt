package parts.wisdom.arcraider.visualization

import javax.swing.JFrame

class ARCWindow(width : Int, height : Int, title : String) : JFrame() {
    init {
        createUI(width, height, title)
    }

    private fun createUI(width : Int, height : Int, title: String) {
        setTitle(title)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(width, height)
        setLocationRelativeTo(null)
    }
}