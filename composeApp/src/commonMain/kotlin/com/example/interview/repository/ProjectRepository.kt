package com.example.interview.repository

import com.example.interview.model.Project
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
class ProjectRepository {

    private val _projects = MutableStateFlow<List<Project>>(emptyList())

    /**
     * Reactive flow of all projects. Collect from this to react to changes.
     */
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

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
}
