package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.SerializedGrid
import parts.wisdom.arcraider.Task
import parts.wisdom.arcraider.loadTaskFromFile
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JToggleButton

private const val GAP_SIZE = 12
private const val GRID_SIZE = 40

typealias TaskGrids = Pair<List<GridPane>, List<GridPane>>

class ARCWindow(width: Int, height: Int, title: String, task: Task) : JFrame() {

    private var taskType = TaskType.TEST
    private val content = JPanel()

    init {
        createUI(width, height, title, task)
    }

    private fun createUI(width: Int, height: Int, title: String, task: Task) {
        setTitle(title)
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(width, height)
        setLocationRelativeTo(null)
        content.layout = BoxLayout(content, BoxLayout.Y_AXIS)
        add(content)

        renderTask(task)
    }

    private fun renderTask(task: Task) {
        content.apply {
            add(Box.createRigidArea(Dimension(0, GAP_SIZE)))
            add(
                    Box.createHorizontalBox().apply {
                        add(Box.createRigidArea(Dimension(GAP_SIZE, 0)))
                        add(createBrowseButton())
                        add(Box.createRigidArea(Dimension(GAP_SIZE, 0)))
                        add(createToggle(task, TaskType.TEST))
                        add(Box.createRigidArea(Dimension(GAP_SIZE, 0)))
                        add(createToggle(task, TaskType.TRAIN))
                        add(Box.createHorizontalGlue())
                    }
            )
            add(
                    Box.createHorizontalBox().apply {
                        val (inputGrids, outputGrids) = createFromTask(task)
                        add(Box.createRigidArea(Dimension(GAP_SIZE, 0)))
                        add(createGridBox("Input", inputGrids))
                        add(Box.createRigidArea(Dimension(GAP_SIZE / 2, 0)))
                        add(Box.createHorizontalGlue())
                        add(Box.createRigidArea(Dimension(GAP_SIZE / 2, 0)))
                        add(createGridBox("Output", outputGrids))
                        add(Box.createRigidArea(Dimension(GAP_SIZE, 0)))
                    }
            )
            add(Box.createVerticalGlue())
        }
    }

    private fun refresh(task: Task) {
        content.removeAll()
        renderTask(task)
        content.revalidate()
        content.repaint()
    }

    private fun createGridBox(label: String, grids: List<GridPane>) =
            Box.createVerticalBox().apply {
                add(
                        Box.createHorizontalBox().apply {
                            add(Box.createHorizontalGlue())
                            add(JLabel(label))
                            add(Box.createHorizontalGlue())
                        }
                )
                add(Box.createRigidArea(Dimension(0, GAP_SIZE / 2)))
                grids.forEach {
                    add(it)
                    add(Box.createRigidArea(Dimension(0, GAP_SIZE / 2)))
                }
            }

    private fun createBrowseButton(): JButton {
        return JButton("Browse").apply {
            addActionListener {
                val fileChooser = JFileChooser()
                val result = fileChooser.showOpenDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val task = loadTaskFromFile(fileChooser.selectedFile)
                    refresh(task)
                }
            }
        }
    }

    private fun createToggle(task: Task, taskType: TaskType): JToggleButton {
        return JToggleButton(taskType.label, this.taskType == taskType).apply {
            addActionListener {
                if (this@ARCWindow.taskType != taskType) {
                    this@ARCWindow.taskType = taskType
                    refresh(task)
                } else {
                    // Prevent toggling if we are already in this state.
                    isSelected = true
                }
            }
        }
    }

    private fun createFromTask(task: Task): TaskGrids {
        fun gridPane(serializedGrid: SerializedGrid): GridPane {
            return GridPane(Grid(serializedGrid)).apply {
                preferredSize = Dimension(GRID_SIZE, GRID_SIZE)
            }
        }

        val arcPairs = when (taskType) {
            TaskType.TEST -> task.test
            TaskType.TRAIN -> task.train
        }
        val inputs = mutableListOf<GridPane>()
        val outputs = mutableListOf<GridPane>()
        arcPairs.forEach { (input, output) ->
            inputs.add(gridPane(input))
            outputs.add(gridPane(output))
        }

        return inputs to outputs
    }
}

private enum class TaskType(val label: String) { TEST("Test"), TRAIN("Train") }