package com.example.interview.api

import com.example.interview.model.Comment
import com.example.interview.model.Pano
import com.example.interview.model.Project
import com.example.interview.model.Room

interface ApiClient {
    suspend fun createProject(project: Project)
    suspend fun updateProject(project: Project)

    suspend fun createRoom(projectId: String, room: Room)
    suspend fun updateRoom(projectId: String, room: Room)

    suspend fun createPano(projectId: String, roomId: String, pano: Pano)
    suspend fun deletePano(projectId: String, roomId: String, panoId: String)

    suspend fun createComment(projectId: String, roomId: String, comment: Comment)
    suspend fun updateComment(projectId: String, roomId: String, comment: Comment)
}
