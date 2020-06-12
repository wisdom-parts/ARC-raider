package parts.wisdom.arcraider.visualization

import parts.wisdom.arcraider.SerializedGrid
import parts.wisdom.arcraider.Task
import parts.wisdom.arcraider.loadTaskFromFile
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JToggleButton
import kotlin.math.max

private const val GAP_SIZE_PX = 12
private const val GRID_SIZE_PX = 400

typealias TaskGrids = List<Pair<GridPane, GridPane>>

class ARCWindow(title: String, task: Task) : JFrame() {

    private var taskType = TaskType.TRAIN
    private val content = JPanel()

    init {
        createUI(title, task)
    }

    private fun createUI(title: String, task: Task) {
        setTitle(title)
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
        content.layout = BoxLayout(content, BoxLayout.Y_AXIS)


        // If the environment variable is set, use it, otherwise default to the home directory.
        val arcEnv = System.getenv("ARC_HOME")
        val arcDir = if (arcEnv.isNullOrEmpty()) System.getProperty("user.home") else arcEnv
        val root = JPanel().apply {
            layout = BorderLayout()
            add(
                    FileBrowser(arcDir) {
                        refresh(loadTaskFromFile(it))
                    },
                    BorderLayout.WEST
            )
            add(content, BorderLayout.CENTER)
        }
        add(root)

        renderTask(task)
        pack()
    }

    private fun renderTask(task: Task) {
        content.apply {
            add(Box.createRigidArea(Dimension(0, GAP_SIZE_PX)))
            add(
                    Box.createHorizontalBox().apply {
                        add(Box.createRigidArea(Dimension(GAP_SIZE_PX, 0)))
                        add(createToggle(task, TaskType.TRAIN))
                        add(Box.createRigidArea(Dimension(GAP_SIZE_PX, 0)))
                        add(createToggle(task, TaskType.TEST))
                        add(Box.createHorizontalGlue())
                    }
            )
            add(
                    Box.createHorizontalBox().apply {
                        add(Box.createRigidArea(Dimension(GAP_SIZE_PX, 0)))
                        add(Box.createHorizontalGlue())
                        add(JLabel("Input"))
                        add(Box.createHorizontalGlue())
                        add(JLabel("Output"))
                        add(Box.createHorizontalGlue())
                        add(Box.createRigidArea(Dimension(GAP_SIZE_PX, 0)))
                    }
            )
            val gridPairs = createFromTask(task)
            gridPairs.forEach { (inputGrid, outputGrid) ->
                add(
                        createGridBox(inputGrid, outputGrid)
                )
                add(Box.createRigidArea(Dimension(0, GAP_SIZE_PX)))
            }
            add(Box.createRigidArea(Dimension(0, GAP_SIZE_PX)))
            add(Box.createVerticalGlue())
        }
    }

    private fun refresh(task: Task) {
        content.removeAll()
        renderTask(task)
        content.revalidate()
        content.repaint()
    }

    private fun createGridBox(inputGrid: GridPane, outputGrid: GridPane) =
            Box.createHorizontalBox().apply {
                add(Box.createRigidArea(Dimension(GAP_SIZE_PX, 0)))
                add(inputGrid)
                add(Box.createHorizontalGlue())
                add(Box.createRigidArea(Dimension(GAP_SIZE_PX * 2, 0)))
                add(Box.createHorizontalGlue())
                add(outputGrid)
                add(Box.createRigidArea(Dimension(GAP_SIZE_PX, 0)))
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
        fun gridPane(serializedGrid: SerializedGrid, isOutput: Boolean): GridPane {
            val grid = Grid(serializedGrid)
            return GridPane(grid, isOutput).apply {
                val largerDimen = max(grid.widthSquares, grid.heightSquares)
                val height = (GRID_SIZE_PX / largerDimen) * grid.heightSquares
                val width = (GRID_SIZE_PX / largerDimen) * grid.widthSquares
                preferredSize = Dimension(width, height)
            }
        }

        val arcPairs = when (taskType) {
            TaskType.TEST -> task.test
            TaskType.TRAIN -> task.train
        }
        return arcPairs.map { (input, output) ->
            gridPane(input, false) to gridPane(output, true)
        }
    }
}

private enum class TaskType(val label: String) { TEST("Test"), TRAIN("Train") }