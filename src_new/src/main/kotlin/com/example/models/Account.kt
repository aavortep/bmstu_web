package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Account(var id: Int = -1, var fio: String? = null, var phone: String? = null,
                   var mail: String? = null, var password: String? = null,
                   var type: String? = null)


object Accounts : Table() {
    val id = integer("id").autoIncrement()
    val fio = text("fio")
    val phone = text("phone")
    val mail = text("mail")
    val password = text("password")
    val type = text("type")

    override val primaryKey = PrimaryKey(id)
}
