package com.example.interview.repository

import com.example.interview.api.ApiClient
import com.example.interview.model.Comment
import com.example.interview.model.Pano
import com.example.interview.model.Project
import com.example.interview.model.Room
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository for managing projects. Uses in-memory storage with reactive StateFlow.
 *
 * This is the single source of truth for project data. Project is the aggregate root,
 * so all mutations to rooms, panos, and comments must be done through Project's
 * copyByXyz methods and then saved via this repository.
 */
class ProjectRepository(
    private val apiClient: ApiClient
) {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())

    /**
     * Reactive flow of all projects. Collect from this to react to changes.
     */
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    init {
        populateSampleData()
    }

    /**
     * Saves or updates a project. If a project with the same ID exists, it is replaced.
     * If it's a new project, it is added to the list.
     *
     * @param project The project (aggregate root) to save
     */
    fun save(project: Project) {
        val currentProjects = _projects.value
        val existingIndex = currentProjects.indexOfFirst { it.id == project.id }

        _projects.value = if (existingIndex >= 0) {
            currentProjects.toMutableList().apply {
                this[existingIndex] = project
            }
        } else {
            currentProjects + project
        }
    }

    /**
     * Uploads a project to the server.
     * TODO: Implement actual upload logic using ApiClient
     */
    suspend fun uploadProject(projectId: String) {
        val project = _projects.value.find { it.id == projectId } ?: return
        // TODO: Use apiClient to upload project, rooms, panos, and comments
        // For now, just simulate upload delay
        delay(2000)
    }

    private fun populateSampleData() {
        // Sample Project 1: Water Damage Assessment
        val sampleProject1 = Project.make(
            name = "123 Main St - Water Damage",
            id = "sample-project-1"
        )
            .copyByAddingRoom(Room.make(name = "Living Room", id = "room-1"))
            .copyByAddingRoom(Room.make(name = "Kitchen", id = "room-2"))
            .copyByAddingRoom(Room.make(name = "Master Bedroom", id = "room-3"))
            .copyBySettingPanoToRoom("room-1", Pano(id = "pano-1", imageData = byteArrayOf(1, 2, 3)))
            .copyByAddingCommentToRoom("room-1", Comment(id = "comment-1", text = "Water stain visible on ceiling"))
            .copyByAddingCommentToRoom("room-1", Comment(id = "comment-2", text = "Carpet is damp near window"))
            .copyByAddingCommentToRoom("room-2", Comment(id = "comment-3", text = "Under sink damage observed"))

        // Sample Project 2: Empty project for testing
        val sampleProject2 = Project.make(
            name = "456 Oak Ave - Inspection",
            id = "sample-project-2"
        )

        _projects.value = listOf(sampleProject1, sampleProject2)
    }
}
