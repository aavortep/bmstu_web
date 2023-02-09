package com.example.dao

import com.example.models.*
import java.io.File
import java.io.InputStream
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init() {
        val inputStream: InputStream = File("config.txt").inputStream()
        val lineList = mutableListOf<String>()
        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        val user = lineList[0]
        val pass = lineList[1]
        val url = lineList[2]
        val className = lineList[3]  // driver

        val database = Database.connect(url, className, user, pass)
        transaction(database) {
            SchemaUtils.create(Accounts)
            SchemaUtils.create(RehBases)
            SchemaUtils.create(Rooms)
            SchemaUtils.create(Equipment)
            SchemaUtils.create(Rehearsals)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}