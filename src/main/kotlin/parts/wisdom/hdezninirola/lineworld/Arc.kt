package parts.wisdom.hdezninirola.lineworld

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

typealias SerializedGrid = List<List<Int>>

@Serializable
data class Task(
        val train: List<ArcPair>,
        val test: List<ArcPair>
)

@Serializable
data class ArcPair(
        val input: SerializedGrid,
        val output: SerializedGrid
)

fun loadTasksFromDir(dir: File): List<Task> {
    TODO()
}

fun loadTaskFromFile(file: File): Task {
    return Json.decodeFromString(Task.serializer(), file.readText())
}

fun loadTaskFromString(taskJson: String): Task {
    TODO()
}