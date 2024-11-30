package ua.edu.lntu.task_manager

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import java.util.Calendar

class TaskManager : ViewModel() {

    var tasks by mutableStateOf(listOf<Task>())

    fun addTask(title: String, description: String, author: String, dateToDone: String) {
        val newTask = Task(
            id = tasks.size + 1,
            title = title,
            description = description,
            author = author,
            date_to_done = dateToDone
        )
        tasks = tasks + newTask
    }

    fun deleteTask(task: Task) {
        tasks = tasks.filter { it.id != task.id }
    }

    fun updateTaskStatus(task: Task, newStatus: TaskStatus) {
        val updatedTasks = tasks.map {
            if (it.id == task.id) it.copy(status = newStatus)
            else it
        }
        tasks = updatedTasks
    }
}

@Composable
fun HomeScreen(navController: NavController, taskManager: TaskManager) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            "Task Manager",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Button(onClick = { navController.navigate("create_task") },
            modifier = Modifier.fillMaxWidth(0.7f),
            ) {
            Text("Create Task")
        }
        Button(onClick = { navController.navigate("todo") },
            modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("To Do")
        }
        Button(onClick = { navController.navigate("in_progress") },
            modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("In Progress")
        }
        Button(onClick = { navController.navigate("done") },
            modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("Done")
        }
    }
}

@Composable
fun CreateTaskScreen(navController: NavController, taskManager: TaskManager) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var dateToDone by remember { mutableStateOf("") }

    val isFormValid = title.isNotBlank() && description.isNotBlank() && author.isNotBlank() && dateToDone.isNotBlank()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Please enter the task data!",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            value = title, onValueChange = { title = it }, label = { Text("Title") })
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            value = description, onValueChange = { description = it }, label = { Text("Description") })
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            value = author, onValueChange = { author = it }, label = { Text("Author") })

        DatePicker(selectedDate = dateToDone, onDateSelected = { dateToDone = it })

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text("Back",
                    style = MaterialTheme.typography.titleLarge)
            }
            Button(
                onClick = {
                    if (isFormValid) {
                        taskManager.addTask(title, description, author, dateToDone)
                        navController.navigate("home")
                    } else {
                        println("Form is invalid, please fill in all fields.")
                    }
                },
                enabled = isFormValid
            ) {
                Text("Create Task",
                    style = MaterialTheme.typography.titleLarge)
            }
        }

    }
}

@Composable
fun TodoScreen(navController: NavController, taskManager: TaskManager) {
    TaskListScreen(
        navController,
        tasks = taskManager.tasks,
        filter = { it.status == TaskStatus.TODO },
        title = "To Do",
        taskManager = taskManager
    )
}

@Composable
fun InProgressScreen(navController: NavController, taskManager: TaskManager) {
    TaskListScreen(
        navController,
        tasks = taskManager.tasks,
        filter = { it.status == TaskStatus.IN_PROGRESS },
        title = "In Progress",
        taskManager = taskManager
    )
}

@Composable
fun DoneScreen(navController: NavController, taskManager: TaskManager) {
    TaskListScreen(
        navController,
        tasks = taskManager.tasks,
        filter = { it.status == TaskStatus.DONE },
        title = "Done",
        taskManager = taskManager
    )
}


@Composable
fun TaskListScreen(
    navController: NavController,
    tasks: List<Task>,
    filter: (Task) -> Boolean,
    title: String,
    taskManager: TaskManager
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text("Back",
                    style = MaterialTheme.typography.headlineMedium)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp),
                textAlign = TextAlign.End
            )
        }
        taskManager.tasks.filter(filter).forEach { task ->
            TaskCard(task,
                onUpdate = { updatedTask ->
                    taskManager.tasks = tasks.map {
                        if (it.id == updatedTask.id) updatedTask else it
                    }
                },
                onDelete = { deletedTask ->
                    taskManager.tasks = tasks.filter { it.id != deletedTask.id }
                })
            Spacer(modifier = Modifier
                .height(15.dp)
                .zIndex(2.0f))
        }

    }
}

@Composable
fun TaskCard(task: Task,
             onUpdate: (Task) -> Unit,
             onDelete: (Task) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Title: ${task.title}", style = MaterialTheme.typography.titleLarge)
                Text("Description: ${task.description}", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { showDialog = true }) {
                Text("View")
            }
        }
    }

    // Модальное окно
    if (showDialog) {
        EditTaskDialog(
            task = task,
            onDismiss = { showDialog = false },
            onUpdate = { updatedTask ->
                onUpdate(updatedTask)
                showDialog = false
            },
            onDelete = {
                onDelete(task)
                showDialog = false
            }
        )
    }
}

@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onUpdate: (Task) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var author by remember { mutableStateOf(task.author) }
    var dateToDone by remember { mutableStateOf(task.date_to_done) }
    var status by remember { mutableStateOf(task.status) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Task", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                DropDownMenuForStates(status = status, onStatusChanged = { newStatus ->
                    status = newStatus // Обновляем статус задачи
                })

                DatePicker(selectedDate = dateToDone, onDateSelected = { dateToDone = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(
                        task.copy(
                            title = title,
                            description = description,
                            author = author,
                            date_to_done = dateToDone,
                            status = status
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Button(onClick = { onDelete() }) {
                    Text("Delete")
                }
            }
        }
    )
}


@Composable
fun DatePicker(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var showDatePicker by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { showDatePicker = true }) {
            Text(text = "Select Date")
        }
        Text(
            text = selectedDate.ifEmpty { "No date selected" },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                onDateSelected(formattedDate)
                showDatePicker = false
            },
            year,
            month,
            day
        ).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuForStates(status: TaskStatus, onStatusChanged: (TaskStatus) -> Unit) {
    val statuses = TaskStatus.values().toList()

    var isExpanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(status) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedStatus.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                statuses.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(text = status.name) },
                        onClick = {
                            selectedStatus = status
                            onStatusChanged(selectedStatus)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}