package parts.wisdom.arcraider

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Represents a grid as a list of rows of `ArcColor` ordinals.
 */
typealias SerializedGrid = List<List<Int>>

@Serializable
data class SerializedTask(
    val train: List<SerializedPair>,
    val test: List<SerializedPair>
)

@Serializable
data class SerializedPair(
    val input: SerializedGrid,
    val output: SerializedGrid
)

enum class ArcColor {
    BLACK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    GRAY,
    FUCHSIA,
    ORANGE,
    TEAL,
    CRIMSON;

    fun toChar(): Char =
        when (this) {
            BLACK -> '.'
            BLUE -> 'b'
            RED -> 'r'
            GREEN -> 'g'
            YELLOW -> 'y'
            GRAY -> 'a'
            FUCHSIA -> 'f'
            ORANGE -> 'o'
            TEAL -> 't'
            CRIMSON -> 'c'
        }

    companion object {
        private val intToColor = List(values().size) { values()[it] }

        fun fromInt(i: Int): ArcColor {
            require(i in intToColor.indices) { "no color for $i" }
            return intToColor[i]
        }

        private val charToColor = values().associateBy { it.toChar() }

        fun fromChar(ch: Char): ArcColor {
            val result = charToColor[ch]
            require(result != null) {
                "unknown color character code '$ch'"
            }
            return result
        }
    }
}

/**
 * A mutable grid of colors. The grid's dimensions are `width, height`, indexed by `[x, y]` where `x in 0 until width` goes left to right
 * and `y in 0 until height` goes top to bottom.
 */
data class ArcGrid(
    val width: Int,
    val height: Int,
    /** Stores `(x, y)` at `y * width + x` */
    private val data: Array<ArcColor>
) {
    val toSerialized: SerializedGrid
        get() = (0 until height).map { y ->
            (0 until width).map { x -> data[y * width + x].ordinal }
        }

    /**
     * Construct an `ArcGrid` with all squares the same color.
     */
    constructor(width: Int, height: Int, color: ArcColor) :
            this(width, height, Array(height * width) { color })

    operator fun get(x: Int, y: Int): ArcColor = data[dataIndex(x, y)]

    operator fun set(x: Int, y: Int, color: ArcColor) {
        data[dataIndex(x, y)] = color
    }

    private fun dataIndex(x: Int, y: Int): Int {
        require(x in 0 until width) { "invalid x=$y" }
        require(y in 0 until height) { "invalid y=$x" }
        return y * width + x
    }

    override fun toString(): String {
        val rows = (0 until height).map { y ->
            data.slice((y*width) until ((y+1)*width))
        }
        val rowStrings = rows.map { row ->
            row.map { it.toChar() }.joinToString("")
        }
        return rowStrings.joinToString("\n")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArcGrid

        if (height != other.height) return false
        if (width != other.width) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + data.contentHashCode()
        return result
    }

    companion object {
        fun fromSerialized(sg: SerializedGrid): ArcGrid {
            val width = sg.map { it.size }.maxOrNull() ?: 0
            val height = sg.size
            val ag = ArcGrid(width, height, ArcColor.BLACK)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    ag[x, y] = ArcColor.fromInt(
                        sg[y].getOrElse(x) { 0 }
                    )
                }
            }
            return ag
        }

        /**
         * For hand-written example grids: Each line of text is a row, where each character represents
         * a color as mapped in `ArcColor.fromChar(c)`.
         */
        // TODO: write a test for this.
        fun fromText(text: String): ArcGrid {
            val rows = text.split('\n')
            val width = rows.map { it.length }.maxOrNull() ?: 0
            val height = rows.size
            val ag = ArcGrid(width, height, ArcColor.BLACK)
            rows.forEachIndexed { y, row ->
                row.forEachIndexed { x, ch ->
                    ag[x, y] = ArcColor.fromChar(ch)
                }
            }
            return ag
        }
    }
}

data class ArcPair(
    val input: ArcGrid,
    val output: ArcGrid
) {
    companion object {
        fun fromSerialized(sp: SerializedPair): ArcPair =
            ArcPair(
                ArcGrid.fromSerialized(sp.input),
                ArcGrid.fromSerialized(sp.output)
            )
    }
}

data class ArcTask(
    val train: List<ArcPair>,
    val test: List<ArcPair>
) {
    companion object {
        fun fromSerialized(st: SerializedTask): ArcTask =
            ArcTask(
                st.train.map { ArcPair.fromSerialized(it) },
                st.test.map { ArcPair.fromSerialized(it) })
    }
}

fun loadTasksFromDir(dir: File): List<SerializedTask> {
    TODO()
}

fun loadTaskFromFile(file: File): ArcTask {
    return ArcTask.fromSerialized(
        Json.decodeFromString(
            SerializedTask.serializer(),
            file.readText()
        )
    )
}

fun loadTaskFromString(taskJson: String): SerializedTask {
    TODO()
}

data class Offset(val dx: Int, val dy: Int) {
    operator fun times(m: Int) = Offset(dx * m, dy * m)
}

enum class Direction(val dx: Int, val dy: Int) {
    RIGHT(1, 0),
    DOWN_RIGHT(1, 1),
    DOWN(0, 1),
    DOWN_LEFT(-1, 1),
    LEFT(-1, 0),
    UP_LEFT(-1, -1);

    operator fun times(m: Int) = Offset(dx * m, dy * m)

    fun rotateClockwise45NDegrees(n: Int): Direction {
        val newOrdinal = (ordinal + n) % values().size
        return values()[newOrdinal]
    }
}