package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class RehearsalBase(var id: Int = -1, var name: String? = null,
                         var address: String? = null, var phone: String? = null,
                         var mail: String? = null, var ownerId: Int = -1)


object RehBases : Table() {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val address = text("address")
    val phone = text("phone")
    val mail = text("mail")
    val ownerId = integer("ownerid")

    override val primaryKey = PrimaryKey(id)
    val foreignKey = ForeignKeyConstraint(ownerId, Accounts.id, ReferenceOption.CASCADE,
        ReferenceOption.CASCADE, "ownerid")
}
