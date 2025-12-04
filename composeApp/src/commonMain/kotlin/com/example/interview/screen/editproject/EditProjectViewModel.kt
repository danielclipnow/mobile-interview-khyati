package com.example.interview.screen.editproject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditProjectViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<EditProjectViewModel.ViewState, EditProjectViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var projectName: String by mutableStateOf("")

        val canSave: Boolean
            get() = projectName.isNotBlank()
    }

    sealed class Action {
        data class UpdateName(val name: String) : Action()
        data object Save : Action()
        data object Cancel : Action()
    }

    override val destination: Destination.EditProject = savedStateHandle.toRoute<Destination.EditProject>()
    override var viewState: ViewState = ViewState()

    private val projectId: String = destination.projectId

    init {
        projectRepository.projects
            .onEach { projects ->
                val project = projects.find { it.id == projectId }
                if (project != null && viewState.projectName.isEmpty()) {
                    viewState.projectName = project.name
                }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun handle(action: Action) {
        when (action) {
            is Action.UpdateName -> {
                viewState.projectName = action.name
            }
            Action.Save -> {
                if (!viewState.canSave) return

                val project = projectRepository.projects.value.find { it.id == projectId } ?: return
                val trimmedName = viewState.projectName.trim()
                val updatedProject = project.copyByUpdatingName(trimmedName)
                projectRepository.save(updatedProject)
                navigator.goBack()
            }
            Action.Cancel -> {
                navigator.goBack()
            }
        }
    }
}
