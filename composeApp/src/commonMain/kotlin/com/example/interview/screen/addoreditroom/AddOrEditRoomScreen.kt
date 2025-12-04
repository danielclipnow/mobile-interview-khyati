package com.example.interview.screen.addoreditroom

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
fun AddOrEditRoomScreen(
    viewModel: AddOrEditRoomViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewState.screenTitle) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.send(AddOrEditRoomViewModel.Action.Cancel) }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.send(AddOrEditRoomViewModel.Action.Save) },
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
                value = viewState.roomName,
                onValueChange = { viewModel.send(AddOrEditRoomViewModel.Action.UpdateName(it)) },
                label = { Text("Room Name") },
                placeholder = { Text("e.g., Living Room, Kitchen, Master Bedroom") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}
