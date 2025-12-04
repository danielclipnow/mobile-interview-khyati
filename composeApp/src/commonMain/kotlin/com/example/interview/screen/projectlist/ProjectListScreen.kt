package com.example.interview.screen.projectlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.interview.model.Project
import com.example.interview.viewmodel.send
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    viewModel: ProjectListViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState

    // Upload progress dialog
    viewState.uploadingProjectName?.let { projectName ->
        UploadingDialog(projectName = projectName)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Projects") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.send(ProjectListViewModel.Action.AddProject) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Project")
            }
        }
    ) { paddingValues ->
        if (viewState.projects.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No projects yet. Tap + to create one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewState.projects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        onClick = {
                            viewModel.send(ProjectListViewModel.Action.SelectProject(project.id))
                        },
                        onUpload = {
                            viewModel.send(ProjectListViewModel.Action.UploadProject(project.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onUpload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${project.rooms.size} rooms",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onUpload) {
                Icon(Icons.Default.CloudUpload, contentDescription = "Upload Project")
            }
        }
    }
}

@Composable
private fun UploadingDialog(projectName: String) {
    AlertDialog(
        onDismissRequest = { /* Non-dismissable */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        title = {
            Text("Uploading Project")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator()
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = { /* No buttons - auto-dismisses */ }
    )
}
