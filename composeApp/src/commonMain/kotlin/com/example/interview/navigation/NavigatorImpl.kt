package com.example.interview.navigation

import androidx.navigation.NavHostController

/**
 * Implementation of Navigator that wraps NavHostController.
 * This allows ViewModels to navigate without direct dependency on Compose Navigation.
 */
class NavigatorImpl(
    private val navController: NavHostController
) : Navigator {

    override fun navigate(to: Destination, from: Destination?) {
        navController.navigate(to)
    }

    override fun goBack() {
        navController.popBackStack()
    }
}
