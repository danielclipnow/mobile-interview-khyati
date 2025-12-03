package com.example.interview.di

import com.example.interview.repository.ProjectRepository
import com.example.interview.screen.projectlist.ProjectListViewModel
import com.example.interview.screen.projectdetail.ProjectDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Main Koin module for the application.
 * Defines all dependency bindings.
 */
val appModule = module {
    // Repository - single instance shared across the app
    single { ProjectRepository() }

    // ViewModels
    viewModel { parameters ->
        ProjectListViewModel(
            savedStateHandle = parameters.get(),
            navigator = get(),
            projectRepository = get()
        )
    }

    viewModel { parameters ->
        ProjectDetailViewModel(
            savedStateHandle = parameters.get(),
            navigator = get(),
            projectRepository = get()
        )
    }
}
