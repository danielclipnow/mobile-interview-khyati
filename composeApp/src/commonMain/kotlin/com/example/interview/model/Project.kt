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

    fun copyByAddingCommentToRoom(roomId: String, comment: Comment): Project {
        return copy(rooms = rooms.map { room ->
            if (room.id == roomId) room.copy(comments = room.comments + comment)
            else room
        })
    }

    fun copyBySettingPanoToRoom(roomId: String, pano: Pano): Project {
        return copy(rooms = rooms.map { room ->
            if (room.id == roomId) room.copy(pano = pano)
            else room
        })
    }

    fun copyByUpdatingRoomName(roomId: String, name: String): Project {
        return copy(rooms = rooms.map { room ->
            if (room.id == roomId) room.copy(name = name)
            else room
        })
    }

    fun copyByRemovingPanoFromRoom(roomId: String): Project {
        return copy(rooms = rooms.map { room ->
            if (room.id == roomId) room.copy(pano = null)
            else room
        })
    }

    fun copyByUpdatingName(name: String): Project {
        return copy(name = name)
    }

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun make(name: String, id: String = Uuid.random().toString()): Project {
            return Project(id = id, name = name)
        }
    }
}
