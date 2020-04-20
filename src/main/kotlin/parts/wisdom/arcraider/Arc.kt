package parts.wisdom.arcraider

import java.io.File

val arcData = File(
    checkNotNull(System.getenv("ARC_DATA"),
        { "ARC_DATA environment variable is not set" })
)

data class Task(
    val trainingPairs: List<ArcPair>,
    val testPairs: List<ArcPair>
)

data class ArcPair(
    val input: ArcGrid,
    val output: ArcGrid
)

class ArcGrid() {
}

fun loadTasksFromDir(dir: File): List<Task> {
    TODO()
}

fun loadTaskFromFile(file: File): Task {
    TODO()
}

fun loadTaskFromString(taskJson: String): Task {
    TODO()
}