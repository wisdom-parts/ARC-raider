package parts.wisdom.arcraider.visualization

import java.awt.Dimension
import java.io.File
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.event.TreeExpansionEvent
import javax.swing.event.TreeExpansionListener
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class FileBrowser(dir: String, onFileSelected: (File) -> Unit = { }) : JPanel() {

    private lateinit var tree: JTree

    private val nodeExpansionListener = object: TreeExpansionListener {
        override fun treeExpanded(event: TreeExpansionEvent?) {
            val subtree = (event?.path?.lastPathComponent as? DefaultMutableTreeNode) ?: return
            if (subtree.depth < 2) {
                for (child in subtree.children()) {
                    TreeExplorer(child as DefaultMutableTreeNode).exploreNodes(3)
                }
            }
        }

        override fun treeCollapsed(event: TreeExpansionEvent?) = Unit
    }

    init {
        val rootFile = File(dir)
        val rootNode = DefaultMutableTreeNode(RootFileNode(rootFile))
        tree = JTree(DefaultTreeModel(rootNode))
        tree.showsRootHandles = true
        tree.addTreeExpansionListener(nodeExpansionListener)
        tree.addTreeSelectionListener {
            val node = tree.lastSelectedPathComponent as? DefaultMutableTreeNode
            if (node?.isLeaf == true) {
                onFileSelected(node.file())
            }
        }

        val scrollPane = JScrollPane(tree).apply {
            preferredSize = Dimension(200, 500)
        }
        add(scrollPane)
        TreeExplorer(rootNode).exploreNodes(3)
    }

    private inner class TreeExplorer(
            private val root: DefaultMutableTreeNode
    ) {

        fun exploreNodes(depth: Int) {
            createChildren(root, depth)
        }

        private fun createChildren(node: DefaultMutableTreeNode, depth: Int) {
            if (depth <= 0) return
            // List all json files in directory, excluding hidden files.
            val files = node.file().listFiles { file ->
                !file.name.startsWith(".") && (file.isDirectory || file.extension == "json")
            } ?: return
            // Alphabetize the files to make it easier to look through.
            files.sort()
            for (file in files) {
                val childNode = DefaultMutableTreeNode(FileNode(file))
                node.add(childNode)
                if (file.isDirectory) {
                    createChildren(childNode, depth - 1)
                }
            }
        }
    }

    private open inner class FileNode(val file: File) {
        override fun toString(): String = if (file.name.isNotEmpty()) file.name else file.absolutePath
    }

    // For the root node, display the whole file path.
    private inner class RootFileNode(file: File) : FileNode(file) {
        override fun toString(): String = file.absolutePath
    }

    // Convenience function so we don't have to cast our nodes all the time.
    private fun DefaultMutableTreeNode.file() = (userObject as FileNode).file
}
