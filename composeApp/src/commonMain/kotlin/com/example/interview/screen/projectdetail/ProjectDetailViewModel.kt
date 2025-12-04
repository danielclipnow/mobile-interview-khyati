package com.example.interview.screen.projectdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.interview.model.Project
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProjectDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<ProjectDetailViewModel.ViewState, ProjectDetailViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var project: Project? by mutableStateOf(null)
    }

    sealed class Action {
        data object AddRoom : Action()
        data class SelectRoom(val roomId: String) : Action()
        data object EditProject : Action()
        data object GoBack : Action()
    }

    override val destination: Destination = savedStateHandle.toRoute<Destination.ProjectDetail>()
    override var viewState: ViewState = ViewState()

    private val projectId: String = (destination as Destination.ProjectDetail).projectId

    init {
        projectRepository.projects
            .onEach { projects ->
                viewState.project = projects.find { it.id == projectId }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun handle(action: Action) {
        when (action) {
            Action.AddRoom -> {
                navigator.navigate(
                    to = Destination.AddOrEditRoom(projectId = projectId),
                    from = destination
                )
            }
            is Action.SelectRoom -> {
                navigator.navigate(
                    to = Destination.RoomDetail(
                        projectId = projectId,
                        roomId = action.roomId
                    ),
                    from = destination
                )
            }
            Action.EditProject -> {
                navigator.navigate(
                    to = Destination.EditProject(projectId = projectId),
                    from = destination
                )
            }
            Action.GoBack -> {
                navigator.goBack()
            }
        }
    }
}
