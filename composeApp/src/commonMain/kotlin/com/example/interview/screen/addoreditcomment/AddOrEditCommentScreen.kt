package com.example.interview.screen.addoreditcomment

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
fun AddOrEditCommentScreen(
    viewModel: AddOrEditCommentViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewState.screenTitle) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.send(AddOrEditCommentViewModel.Action.Cancel) }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.send(AddOrEditCommentViewModel.Action.Save) },
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
                value = viewState.commentText,
                onValueChange = { viewModel.send(AddOrEditCommentViewModel.Action.UpdateText(it)) },
                label = { Text("Comment") },
                placeholder = { Text("Enter your comment...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 5
            )
        }
    }
}
