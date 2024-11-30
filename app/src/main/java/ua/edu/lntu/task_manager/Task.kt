package ua.edu.lntu.task_manager

enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}

data class Task(
    val id: Int,
    var title: String,
    var description: String,
    var author: String,
    var date_to_done: String,
    var status: TaskStatus = TaskStatus.TODO
)
