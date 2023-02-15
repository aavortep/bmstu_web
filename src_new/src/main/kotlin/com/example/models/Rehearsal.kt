package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class Rehearsal(var id: Int = -1, var musicianId: Int = -1, var time: String? = null,
                     var roomId: Int = -1)


object Rehearsals : Table() {
    val id = integer("id").autoIncrement()
    val time = text("time")
    val musicianId = integer("musicianid")
    val roomId = integer("roomid")

    override val primaryKey = PrimaryKey(id)
    val foreignMusician = ForeignKeyConstraint(
        musicianId, Accounts.id, ReferenceOption.CASCADE,
        ReferenceOption.CASCADE, "musicianid")
    val foreignRoom = ForeignKeyConstraint(
        roomId, Rooms.id, ReferenceOption.CASCADE,
        ReferenceOption.CASCADE, "roomid")
}
