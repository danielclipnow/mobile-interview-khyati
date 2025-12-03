package com.example.interview.screen.addcomment

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
fun AddCommentScreen(
    viewModel: AddCommentViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Comment") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.send(AddCommentViewModel.Action.Cancel) }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.send(AddCommentViewModel.Action.Save) },
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
                onValueChange = { viewModel.send(AddCommentViewModel.Action.UpdateText(it)) },
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
