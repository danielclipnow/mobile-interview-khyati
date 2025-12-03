package com.example.interview.screen.projectlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.example.interview.model.Project
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.lifecycle.viewModelScope

class ProjectListViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<ProjectListViewModel.ViewState, ProjectListViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var projects: List<Project> by mutableStateOf(emptyList())
    }

    sealed class Action {
        data object AddProject : Action()
        data class SelectProject(val projectId: String) : Action()
    }

    override val destination: Destination = Destination.ProjectList
    override var viewState: ViewState = ViewState()

    init {
        projectRepository.projects
            .onEach { projects -> viewState.projects = projects }
            .launchIn(viewModelScope)
    }

    override suspend fun handle(action: Action) {
        when (action) {
            Action.AddProject -> {
                val newProject = Project.make(name = "New Project")
                projectRepository.save(newProject)
            }
            is Action.SelectProject -> {
                navigator.navigate(
                    to = Destination.ProjectDetail(projectId = action.projectId),
                    from = destination
                )
            }
        }
    }
}
