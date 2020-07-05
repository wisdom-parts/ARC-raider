package parts.wisdom.deansher.arcraider

import parts.wisdom.arcraider.ArcColor
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
            val width = sg.map { it.size }.max() ?: 0
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
         * For hand-written example grids: Each string is a row, where each character represents
         * a color as mapped in `ArcColor.fromChar(c)`.
         */
        fun fromText(vararg rows: String): ArcGrid {
            val width = rows.map { it.length }.max() ?: 0
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