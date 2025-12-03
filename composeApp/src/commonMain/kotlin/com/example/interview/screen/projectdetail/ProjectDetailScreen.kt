package com.example.interview.screen.projectdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.interview.model.Room
import com.example.interview.viewmodel.send
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState
    val project = viewState.project

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.name ?: "Project") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.send(ProjectDetailViewModel.Action.GoBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.send(ProjectDetailViewModel.Action.AddRoom) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Room")
            }
        }
    ) { paddingValues ->
        if (project == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (project.rooms.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No rooms yet. Tap + to add one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(project.rooms, key = { it.id }) { room ->
                    RoomCard(
                        room = room,
                        onClick = {
                            viewModel.send(ProjectDetailViewModel.Action.SelectRoom(room.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomCard(
    room: Room,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = buildString {
                    append(if (room.pano != null) "Has panorama" else "No panorama")
                    append(" • ${room.comments.size} comments")
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
