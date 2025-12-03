package com.example.interview.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.interview.navigation.Destination
import kotlinx.coroutines.launch

/**
 * Base interface for ViewModels following the MVVM pattern.
 *
 * @param ViewState The type representing the UI state
 * @param Action The type representing user actions/intents
 */
interface ViewModel<ViewState, Action> {

    /**
     * Current state of the view. Should be observed by the UI.
     */
    val viewState: ViewState

    /**
     * Handles an action dispatched from the UI.
     * Called via the send() extension function.
     */
    suspend fun handle(action: Action)

    /**
     * The navigation destination this ViewModel is associated with.
     */
    val destination: Destination
}

/**
 * Extension function to dispatch an action to a ViewModel.
 * This launches a coroutine to handle the action asynchronously.
 *
 * Usage from Composable: viewModel.send(Action.SomeAction)
 */
fun <ViewState, Action> ViewModel<ViewState, Action>.send(action: Action) {
    val scope = if (this is AndroidViewModel) {
        viewModelScope
    } else {
        error("ViewModel must extend androidx.lifecycle.ViewModel")
    }
    scope.launch {
        handle(action)
    }
}

/**
 * Type alias for clarity - all ViewModels should extend this.
 */
typealias AndroidViewModel = androidx.lifecycle.ViewModel
