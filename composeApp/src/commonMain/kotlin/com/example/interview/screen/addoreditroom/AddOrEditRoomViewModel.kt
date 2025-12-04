package com.example.interview.screen.addoreditroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.interview.model.Room
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddOrEditRoomViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<AddOrEditRoomViewModel.ViewState, AddOrEditRoomViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var roomName: String by mutableStateOf("")
        var isEditMode: Boolean by mutableStateOf(false)

        val canSave: Boolean
            get() = roomName.isNotBlank()

        val screenTitle: String
            get() = if (isEditMode) "Edit Room" else "Add Room"
    }

    sealed class Action {
        data class UpdateName(val name: String) : Action()
        data object Save : Action()
        data object Cancel : Action()
    }

    override val destination: Destination = savedStateHandle.toRoute<Destination.AddOrEditRoom>()
    override var viewState: ViewState = ViewState()

    private val projectId: String = (destination as Destination.AddOrEditRoom).projectId
    private val roomId: String? = (destination as Destination.AddOrEditRoom).roomId

    init {
        viewState.isEditMode = roomId != null

        // If editing, load the existing room name
        if (roomId != null) {
            projectRepository.projects
                .onEach { projects ->
                    val project = projects.find { it.id == projectId }
                    val room = project?.room(roomId)
                    if (room != null && viewState.roomName.isEmpty()) {
                        viewState.roomName = room.name
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    override suspend fun handle(action: Action) {
        when (action) {
            is Action.UpdateName -> {
                viewState.roomName = action.name
            }
            Action.Save -> {
                if (!viewState.canSave) return

                val project = projectRepository.projects.value.find { it.id == projectId } ?: return
                val trimmedName = viewState.roomName.trim()

                val updatedProject = if (roomId != null) {
                    // Edit mode - update existing room's name
                    project.copyByUpdatingRoomName(roomId, trimmedName)
                } else {
                    // Add mode - create new room
                    val newRoom = Room.make(name = trimmedName)
                    project.copyByAddingRoom(newRoom)
                }

                projectRepository.save(updatedProject)
                navigator.goBack()
            }
            Action.Cancel -> {
                navigator.goBack()
            }
        }
    }
}
