package com.example.interview.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed class defining all navigation destinations in the app.
 * Uses kotlinx.serialization for type-safe navigation.
 */
@Serializable
sealed class Destination {

    @Serializable
    data object ProjectList : Destination()

    @Serializable
    data class ProjectDetail(val projectId: String) : Destination()

    @Serializable
    data class RoomDetail(val projectId: String, val roomId: String) : Destination()

    @Serializable
    data class AddOrEditComment(
        val projectId: String,
        val roomId: String,
        val commentId: String? = null // null = add mode, non-null = edit mode
    ) : Destination()

    @Serializable
    data class AddOrEditRoom(
        val projectId: String,
        val roomId: String? = null // null = add mode, non-null = edit mode
    ) : Destination()

    @Serializable
    data class EditProject(val projectId: String) : Destination()
}
