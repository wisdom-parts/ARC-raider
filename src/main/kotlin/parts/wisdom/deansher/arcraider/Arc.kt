package parts.wisdom.deansher.arcraider

import parts.wisdom.arcraider.SerializedGrid
import parts.wisdom.arcraider.ArcPair as SerializedPair
import parts.wisdom.arcraider.Task as SerializedTask

/**
 * A grid with dimensions `width, height`, indexed by `[x, y]` where `x in 0 until width` goes left to right
 * and `y in 0 until height` goes top to bottom.
 */
data class ArcGrid(
    val width: Int,
    val height: Int,
    /** Stores `(x, y)` at `y * width + x` */
    private val data: Array<ArcColor>
) {
    /**
     * Construct a `Grid` with all squares the same color.
     */
    constructor(numRows: Int, numCols: Int, color: ArcColor) :
            this(numRows, numCols, Array(numRows * numCols) { color })

    operator fun get(x: Int, y: Int): ArcColor = data[dataIndex(x, y)]

    operator fun set(x: Int, y: Int, color: ArcColor) {
        data[dataIndex(x, y)] = color
    }

    private fun dataIndex(x: Int, y: Int): Int {
        require(x in 0 until width) { "invalid x=$y" }
        require(y in 0 until height) { "invalid y=$x" }
        return y * width + x
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
            val width = sg.map { it.size }.max() ?: 1
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

enum class ArcColor {
    BLACK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    GRAY,
    FUSCHIA,
    ORANGE,
    TEAL,
    CRIMSON;

    companion object {
        private val intToColor = MutableList(values().size) { BLACK }.apply {
            for (c in values()) {
                set(c.ordinal, c)
            }
        }

        fun fromInt(i: Int): ArcColor {
            require(i in 0 until intToColor.size) { "no color for $i" }
            return intToColor[i]
        }
    }
}
