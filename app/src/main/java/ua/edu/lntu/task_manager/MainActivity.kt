package ua.edu.lntu.task_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Ініціалізація NavController
            val navController = rememberNavController()
            val taskManager = TaskManager()

            // Використання NavHost для навігації
            NavHost(navController = navController, startDestination = "home") {
                // Додати екрани та навігацію
                composable("home") { HomeScreen(navController, taskManager) }
                composable("create_task") { CreateTaskScreen(navController, taskManager) }
                composable("todo") { TodoScreen(navController, taskManager) }
                composable("in_progress") { InProgressScreen(navController, taskManager) }
                composable("done") { DoneScreen(navController, taskManager) }
            }
        }
    }
}
