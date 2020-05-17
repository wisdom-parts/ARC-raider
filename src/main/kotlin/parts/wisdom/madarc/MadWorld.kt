package parts.wisdom.madarc

import me.joypri.to
import parts.wisdom.arcraider.*

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
    val canvas = Array(myGrid.width) { Array(myGrid.height) {1} }
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    println("-----")
    myGrid.children.add(myLine)
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    println("-----")
    myGrid.children.removeAt(0)
    myGrid.children.add(extensionTransformation.transform(myLine))
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
    println("-----")
    myGrid.children.removeAt(0)
    myGrid.children.add(rotationTransformation.transform(myLine))
    RectangleStringRepresentation().draw(myGrid, canvas)
    println(canvas.contentDeepToString())
}
