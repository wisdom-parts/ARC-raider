package parts.wisdom.djbrick.lineworld

import parts.wisdom.arcraider.Generator
import parts.wisdom.arcraider.visualization.Grid

interface Game {

    fun run(gameProgress: GameProgress)

    fun getInputGrid() : Grid

    fun getOutputGrid() : Grid

    fun getCurrentGrid() : Grid
}