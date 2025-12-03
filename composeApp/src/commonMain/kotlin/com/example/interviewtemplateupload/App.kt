package com.example.interviewtemplateupload

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.interview.di.appModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        MaterialTheme {
            // Navigation will be set up in later phases
            // For now, just show a placeholder
            Text("Interview App")
        }
    }
}