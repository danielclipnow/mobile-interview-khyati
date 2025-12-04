package com.example.interview.screen.projectlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.interview.model.Project
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProjectListViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<ProjectListViewModel.ViewState, ProjectListViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var projects: List<Project> by mutableStateOf(emptyList())
        var uploadingProjectName: String? by mutableStateOf(null)
    }

    sealed class Action {
        data object AddProject : Action()
        data class SelectProject(val projectId: String) : Action()
        data class UploadProject(val projectId: String) : Action()
    }

    override val destination: Destination.ProjectList = Destination.ProjectList
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
            is Action.UploadProject -> {
                val project = projectRepository.projects.value.find { it.id == action.projectId } ?: return
                viewState.uploadingProjectName = project.name
                projectRepository.uploadProject(action.projectId)
                viewState.uploadingProjectName = null
            }
        }
    }
}
