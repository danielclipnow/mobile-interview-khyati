package com.example.interview.screen.addoreditcomment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.interview.model.Comment
import com.example.interview.navigation.Destination
import com.example.interview.navigation.Navigator
import com.example.interview.repository.ProjectRepository
import com.example.interview.viewmodel.AndroidViewModel
import com.example.interview.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddOrEditCommentViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigator: Navigator,
    private val projectRepository: ProjectRepository
) : ViewModel<AddOrEditCommentViewModel.ViewState, AddOrEditCommentViewModel.Action>, AndroidViewModel() {

    class ViewState {
        var commentText: String by mutableStateOf("")
        var isEditMode: Boolean by mutableStateOf(false)

        val canSave: Boolean
            get() = commentText.isNotBlank()

        val screenTitle: String
            get() = if (isEditMode) "Edit Comment" else "Add Comment"
    }

    sealed class Action {
        data class UpdateText(val text: String) : Action()
        data object Save : Action()
        data object Cancel : Action()
    }

    override val destination: Destination.AddOrEditComment = savedStateHandle.toRoute<Destination.AddOrEditComment>()
    override var viewState: ViewState = ViewState()

    private val projectId: String = destination.projectId
    private val roomId: String = destination.roomId
    private val commentId: String? = destination.commentId

    init {
        viewState.isEditMode = commentId != null

        // If editing, load the existing comment text
        if (commentId != null) {
            projectRepository.projects
                .onEach { projects ->
                    val project = projects.find { it.id == projectId }
                    val room = project?.room(roomId)
                    val comment = room?.comments?.find { it.id == commentId }
                    if (comment != null && viewState.commentText.isEmpty()) {
                        viewState.commentText = comment.text
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun handle(action: Action) {
        when (action) {
            is Action.UpdateText -> {
                viewState.commentText = action.text
            }
            Action.Save -> {
                if (!viewState.canSave) return

                val project = projectRepository.projects.value.find { it.id == projectId } ?: return
                val trimmedText = viewState.commentText.trim()

                val updatedProject = if (commentId != null) {
                    // Edit mode - update existing comment
                    project.copyByUpdatingCommentInRoom(roomId, commentId, trimmedText)
                } else {
                    // Add mode - create new comment
                    val comment = Comment(
                        id = Uuid.random().toString(),
                        text = trimmedText
                    )
                    project.copyByAddingCommentToRoom(roomId, comment)
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
