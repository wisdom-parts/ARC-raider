package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.Task
import parts.wisdom.arcraider.loadTaskFromFile
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*

class ARCWindow(width : Int, height : Int, title : String, task : Task) : JFrame() {

    private lateinit var gridPane : GridPane

    init {
        createUI(width, height, title, task)
    }

    private fun createUI(width : Int, height : Int, title: String, task: Task) {
        setTitle(title)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(width, height)
        setLocationRelativeTo(null)

        val content = JPanel()
        content.layout = BoxLayout(content, BoxLayout.Y_AXIS)

        gridPane = GridPane(Grid(task.test[0].input)).apply {
            preferredSize = Dimension(800, 540)
        }
        val buttonPanel = renderButtonPanel(content)

        content.add(buttonPanel)
        content.add(gridPane)
        add(content)
    }

    private fun renderButtonPanel(content : JPanel) : JPanel {
        val buttonPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            preferredSize = Dimension(800, 60)
        }

        val chooserButton = JButton("Browse").apply {
            preferredSize = Dimension(100, 40)
            addActionListener {
                val fileChooser = JFileChooser()
                val result = fileChooser.showOpenDialog(content)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val task = loadTaskFromFile(fileChooser.selectedFile)
                    gridPane.resetGrid(Grid(task.test[0].input))
                }
            }
        }

        buttonPanel.add(chooserButton)
        return buttonPanel
    }
}