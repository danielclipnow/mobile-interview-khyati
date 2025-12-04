package com.example.interview.screen.editproject

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.interview.viewmodel.send
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProjectScreen(
    viewModel: EditProjectViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Project") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.send(EditProjectViewModel.Action.Cancel) }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.send(EditProjectViewModel.Action.Save) },
                        enabled = viewState.canSave
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewState.projectName,
                onValueChange = { viewModel.send(EditProjectViewModel.Action.UpdateName(it)) },
                label = { Text("Project Name") },
                placeholder = { Text("e.g., 123 Main St - Water Damage") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
