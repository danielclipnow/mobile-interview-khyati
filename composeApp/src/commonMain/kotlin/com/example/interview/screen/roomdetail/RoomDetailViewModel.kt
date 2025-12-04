package com.example.interview.screen.roomdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.interview.model.Pano
import com.example.interview.model.Room
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class RoomDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<RoomDetailViewModel.ViewState, RoomDetailViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var room: Room? by mutableStateOf(null)
    }

    sealed class Action {
        data class AddPano(val imageData: ByteArray) : Action()
        data object AddComment : Action()
        data object EditRoom : Action()
        data object GoBack : Action()
    }

    override val destination: Destination = savedStateHandle.toRoute<Destination.RoomDetail>()
    override var viewState: ViewState = ViewState()

    private val projectId: String = (destination as Destination.RoomDetail).projectId
    private val roomId: String = (destination as Destination.RoomDetail).roomId

    init {
        projectRepository.projects
            .onEach { projects ->
                val project = projects.find { it.id == projectId }
                viewState.room = project?.room(roomId)
            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun handle(action: Action) {
        when (action) {
            is Action.AddPano -> {
                val project = projectRepository.projects.value.find { it.id == projectId } ?: return
                val pano = Pano(id = Uuid.random().toString(), imageData = action.imageData)
                val updatedProject = project.copyByUpdatingRoom(roomId) { room ->
                    room.copyBySettingPano(pano)
                }
                projectRepository.save(updatedProject)
            }
            Action.AddComment -> {
                navigator.navigate(
                    to = Destination.AddComment(
                        projectId = projectId,
                        roomId = roomId
                    ),
                    from = destination
                )
            }
            Action.EditRoom -> {
                navigator.navigate(
                    to = Destination.AddOrEditRoom(
                        projectId = projectId,
                        roomId = roomId
                    ),
                    from = destination
                )
            }
            Action.GoBack -> {
                navigator.goBack()
            }
        }
    }
}
