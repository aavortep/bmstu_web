package com.example.models

import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

data class Gear(var id: Int = -1, var roomId: Int = -1, var type: String? = null,
                var brand: String? = null, var amount: Int = 0)


object Equipment : Table() {
    val id = integer("id").autoIncrement()
    val type = text("type")
    val brand = text("brand")
    val amount = integer("amount")
    val roomId = integer("roomid")

    override val primaryKey = PrimaryKey(id)
    val foreignKey = ForeignKeyConstraint(
        roomId, Rooms.id, ReferenceOption.CASCADE,
        ReferenceOption.CASCADE, "roomid")
}
