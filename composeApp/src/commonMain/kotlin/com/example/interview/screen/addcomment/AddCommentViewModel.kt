package com.example.interview.screen.addcomment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.interview.model.Comment
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddCommentViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<AddCommentViewModel.ViewState, AddCommentViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var commentText: String by mutableStateOf("")

        val canSave: Boolean
            get() = commentText.isNotBlank()
    }

    sealed class Action {
        data class UpdateText(val text: String) : Action()
        data object Save : Action()
        data object Cancel : Action()
    }

    override val destination: Destination = savedStateHandle.toRoute<Destination.AddComment>()
    override var viewState: ViewState = ViewState()

    private val projectId: String = (destination as Destination.AddComment).projectId
    private val roomId: String = (destination as Destination.AddComment).roomId

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun handle(action: Action) {
        when (action) {
            is Action.UpdateText -> {
                viewState.commentText = action.text
            }
            Action.Save -> {
                if (!viewState.canSave) return

                val project = projectRepository.projects.value.find { it.id == projectId } ?: return
                val comment = Comment(
                    id = Uuid.random().toString(),
                    text = viewState.commentText.trim()
                )
                val updatedProject = project.copyByUpdatingRoom(roomId) { room ->
                    room.copyByAddingComment(comment)
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
