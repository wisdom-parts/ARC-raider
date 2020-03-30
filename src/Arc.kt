package parts.wisdom.arcweld

import java.io.File

val arcHome = File(
    checkNotNull(System.getenv("ARC_HOME"),
        { "ARC_HOME environment variable is not set" })
)

class Task

fun loadTasksFromDir(dir: File): List<Task> {
    TODO()
}

fun loadTaskFromFile(file: File): Task {
    TODO()
}

fun loadTaskFromString(taskJson: String): Task {
    TODO()
}