package com.example.interview.api

import com.example.interview.model.Comment
import com.example.interview.model.Pano
import com.example.interview.model.Project
import com.example.interview.model.Room

class ApiClientImpl : ApiClient {

    override suspend fun createProject(project: Project) {
        // TODO: Implement API call
    }

    override suspend fun updateProject(project: Project) {
        // TODO: Implement API call
    }

    override suspend fun createRoom(projectId: String, room: Room) {
        // TODO: Implement API call
    }

    override suspend fun updateRoom(projectId: String, room: Room) {
        // TODO: Implement API call
    }

    override suspend fun createPano(projectId: String, roomId: String, pano: Pano) {
        // TODO: Implement API call
    }

    override suspend fun deletePano(projectId: String, roomId: String, panoId: String) {
        // TODO: Implement API call
    }

    override suspend fun createComment(projectId: String, roomId: String, comment: Comment) {
        // TODO: Implement API call
    }

    override suspend fun updateComment(projectId: String, roomId: String, comment: Comment) {
        // TODO: Implement API call
    }
}
