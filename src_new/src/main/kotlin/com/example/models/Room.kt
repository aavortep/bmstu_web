package com.example.models

import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

data class Room(var id: Int = -1, var name: String? = null, var type: String? = null,
                var area: Int = 0, var cost: Int = 0, var baseId: Int = -1)


object Rooms : Table() {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val type = text("type")
    val area = integer("area")
    val cost = integer("cost")
    val baseId = integer("baseid")

    override val primaryKey = PrimaryKey(id)
    val foreignKey = ForeignKeyConstraint(
        baseId, RehBases.id, ReferenceOption.CASCADE,
        ReferenceOption.CASCADE, "baseid")
}
