package com.example.interview.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Project(
    val id: String,
    val name: String,
    val rooms: List<Room> = emptyList()
) {
    fun room(id: String): Room? = rooms.find { it.id == id }

    fun copyByAddingRoom(room: Room): Project {
        return copy(rooms = rooms + room)
    }

    fun copyByUpdatingRoom(roomId: String, updater: (Room) -> Room): Project {
        return copy(rooms = rooms.map { if (it.id == roomId) updater(it) else it })
    }

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun make(name: String, id: String = Uuid.random().toString()): Project {
            return Project(id = id, name = name)
        }
    }
}
