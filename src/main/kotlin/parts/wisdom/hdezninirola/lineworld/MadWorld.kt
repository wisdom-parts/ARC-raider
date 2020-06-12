package parts.wisdom.hdezninirola.lineworld

import me.joypri.to
import parts.wisdom.arcraider.*
import parts.wisdom.arcraider.visualization.ARCWindow

/**
 *
 * This file is just an example of how to use our building blocks.
 */


val myGrid = Rectangle(
        X to 0,
        Y to 0,
        Height to 6,
        Width to 6,
        TheColor to Color.BLACK
)

val myLine = Line(
        X to 1,
        Y to 1,
        TheColor to Color.RED,
        TheDirection to Direction.RIGHT,
        Length to 3
)

val extensionTransformation = LineExtensionTransformation()
val rotationTransformation = LineRotationTransformation()

fun main() {
    val grids = mutableListOf<Array<Array<Int>>>()
    val canvas = Array(myGrid.width) { Array(myGrid.height) {1} }
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    println("-----")
    myGrid.children.add(myLine)
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    val origGrid = canvas.copy()
    grids.add(origGrid)
    println("-----")
    myGrid.children.removeAt(0)
    myGrid.children.add(extensionTransformation.transform(myLine))
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    grids.add(canvas.copy())
    println("-----")
    myGrid.children.removeAt(0)
    myGrid.children.add(rotationTransformation.transform(myLine))
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    grids.add(origGrid)
    grids.add(canvas.copy())

    ARCWindow("MadWorld", gridToTask(grids.filterIndexed { index, _ -> index % 2 == 0 }, grids.filterIndexed { index, _ -> index % 2 == 1})).isVisible = true
}

fun Array<Array<Int>>.copy(): Array<Array<Int>> {
    return Array<Array<Int>>(this.size) { this[it].copyOf() }
}
