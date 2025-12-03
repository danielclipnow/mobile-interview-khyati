package com.example.interview.navigation

/**
 * Navigator interface for handling navigation from ViewModels.
 * ViewModels should call navigate() - screens should NOT call navigation directly.
 */
interface Navigator {

    /**
     * Navigate to a destination.
     *
     * @param to The destination to navigate to
     * @param from The source destination (used for context-aware navigation decisions)
     */
    fun navigate(to: Destination, from: Destination?)

    /**
     * Navigate back to the previous screen.
     */
    fun goBack()
}
