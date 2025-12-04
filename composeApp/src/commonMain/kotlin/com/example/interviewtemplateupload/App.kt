package com.example.interviewtemplateupload

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.interview.di.appModule
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.navigation.NavigatorImpl
import com.example.interview.screen.projectlist.ProjectListScreen
import com.example.interview.screen.projectdetail.ProjectDetailScreen
import com.example.interview.screen.roomdetail.RoomDetailScreen
import com.example.interview.screen.addcomment.AddCommentScreen
import com.example.interview.screen.addoreditroom.AddOrEditRoomScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val navigator = remember(navController) { NavigatorImpl(navController) }

    // Create a module that provides Navigator
    val navigationModule = remember(navigator) {
        module {
            single<Navigator> { navigator }
        }
    }

    KoinApplication(application = {
        modules(appModule, navigationModule)
    }) {
        MaterialTheme {
            NavHost(
                navController = navController,
                startDestination = Destination.ProjectList
            ) {
                composable<Destination.ProjectList> {
                    ProjectListScreen()
                }
                composable<Destination.ProjectDetail> {
                    ProjectDetailScreen()
                }
                composable<Destination.RoomDetail> {
                    RoomDetailScreen()
                }
                composable<Destination.AddComment> {
                    AddCommentScreen()
                }
                composable<Destination.AddOrEditRoom> {
                    AddOrEditRoomScreen()
                }
            }
        }
    }
}