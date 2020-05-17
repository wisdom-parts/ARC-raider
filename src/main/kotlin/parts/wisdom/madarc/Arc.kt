package parts.wisdom.madarc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
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
    val json = Json(JsonConfiguration.Stable)
    return json.parse(Task.serializer(), file.readText())
}

fun loadTaskFromString(taskJson: String): Task {
    TODO()
}