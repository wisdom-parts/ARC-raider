package parts.wisdom.hdezninirola.lineworld

import parts.wisdom.arcraider.Direction

open class RectangleStringRepresentation {
    fun draw(input: Rectangle, canvas: Array<Array<Int>>) {
        for (i in input.x until input.x + input.width) {
            for (j in input.y until input.y + input.height) {
                canvas[i][j] = input.color.ordinal
            }
        }

        for (child in input.children) {
            when (child) {
                is Rectangle -> RectangleStringRepresentation().draw(child, canvas)
                is Line -> LineStringRepresentation().draw(child, canvas)
                is Pixel -> PixelStringRepresentation().draw(child, canvas)
            }
        }
    }
}

open class LineStringRepresentation {
    fun draw(input: Line, canvas: Array<Array<Int>>) {
        for (delta in 0.until(input.length)) {
            when (input.direction) {
                Direction.DOWN -> canvas[input.x + delta][input.y] = input.color.ordinal
                else -> canvas[input.x][input.y + delta] = input.color.ordinal
            }
        }
    }
}

open class PixelStringRepresentation {
    fun draw(input: Pixel, canvas: Array<Array<Int>>) {
        canvas[input.x][input.y] = input.color.ordinal
    }
}

