package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

class DAOAccountImpl : DAOAccount {
    private fun resultRowToAccount(row: ResultRow) = Account(
        id = row[Accounts.id],
        fio = row[Accounts.fio],
        phone = row[Accounts.phone],
        mail = row[Accounts.mail],
        password = row[Accounts.password],
        type = row[Accounts.type],
    )

    override suspend fun allAccounts(): List<Account> = dbQuery {
        Accounts.selectAll().map(::resultRowToAccount)
    }

    override suspend fun account(id: Int): Account? = dbQuery {
        Accounts.select { Accounts.id eq id }.map(::resultRowToAccount).singleOrNull()
    }

    override suspend fun login(mail: String, password: String): Account? = dbQuery {
        Accounts.select { (Accounts.mail eq mail) and (Accounts.password eq password) }
            .map(::resultRowToAccount).singleOrNull()
    }

    override suspend fun addAccount(fio: String, phone: String, mail: String,
                                    password: String, type: String): Account? = dbQuery {
        val insertStatement = Accounts.insert {
            it[Accounts.fio] = fio
            it[Accounts.phone] = phone
            it[Accounts.mail] = mail
            it[Accounts.password] = password
            it[Accounts.type] = type
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAccount)
    }

    override suspend fun deleteAccount(id: Int): Boolean = dbQuery {
        Accounts.deleteWhere { Accounts.id eq id } > 0
    }

    /*val dao: DAOAccount = DAOAccountImpl().apply {
        runBlocking {
            if(allAccounts().isEmpty()) {
                addAccount("Petrova A. A.", "+7-916-988-53-54",
                    "nura.alexevna@yandex.ru", "1234", "musician")
            }
        }
    }*/
}

class DAOBaseImpl : DAOBase {
    private fun resultRowToBase(row: ResultRow) = RehearsalBase(
        id = row[RehBases.id],
        ownerId = row[RehBases.ownerId],
        name = row[RehBases.name],
        address = row[RehBases.address],
        phone = row[RehBases.phone],
        mail = row[RehBases.mail],
    )

    override suspend fun allBases(): List<RehearsalBase> = dbQuery {
        RehBases.selectAll().map(::resultRowToBase)
    }

    override suspend fun basesByAccount(accId: Int): List<RehearsalBase> = dbQuery {
        RehBases.select { RehBases.ownerId eq accId }.map(::resultRowToBase)
    }

    override suspend fun base(id: Int): RehearsalBase? = dbQuery {
        RehBases.select { RehBases.id eq id }.map(::resultRowToBase).singleOrNull()
    }

    override suspend fun baseByAddress(address: String): RehearsalBase? = dbQuery {
        RehBases.select { RehBases.address eq address }.map(::resultRowToBase).singleOrNull()
    }

    override suspend fun addBase(
        ownerId: Int,
        name: String,
        address: String,
        phone: String,
        mail: String
    ): RehearsalBase? = dbQuery {
        val insertStatement = RehBases.insert {
            it[RehBases.ownerId] = ownerId
            it[RehBases.name] = name
            it[RehBases.address] = address
            it[RehBases.phone] = phone
            it[RehBases.mail] = mail
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToBase)
    }

    override suspend fun deleteBase(id: Int): Boolean = dbQuery {
        RehBases.deleteWhere { RehBases.id eq id } > 0
    }

    override suspend fun deleteByAccount(accId: Int): Boolean = dbQuery {
        RehBases.deleteWhere { RehBases.ownerId eq accId } > 0
    }
}

class DAORoomImpl : DAORoom {
    private fun resultRowToRoom(row: ResultRow) = Room(
        id = row[Rooms.id],
        baseId = row[Rooms.baseId],
        name = row[Rooms.name],
        type = row[Rooms.type],
        area = row[Rooms.area],
        cost = row[Rooms.cost],
    )

    override suspend fun allRooms(): List<Room> = dbQuery {
        Rooms.selectAll().map(::resultRowToRoom)
    }

    override suspend fun room(id: Int): Room? = dbQuery {
        Rooms.select { Rooms.id eq id }.map(::resultRowToRoom).singleOrNull()
    }

    override suspend fun roomByBase(baseId: Int, name: String): Room? = dbQuery {
        Rooms.select { (Rooms.baseId eq baseId) and
                (Rooms.name eq name) }.map(::resultRowToRoom).singleOrNull()
    }

    override suspend fun addRoom(
        baseId: Int,
        name: String,
        type: String,
        area: Int,
        cost: Int
    ): Room? = dbQuery {
        val insertStatement = Rooms.insert {
            it[Rooms.baseId] = baseId
            it[Rooms.name] = name
            it[Rooms.type] = type
            it[Rooms.area] = area
            it[Rooms.cost] = cost
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToRoom)
    }

    override suspend fun deleteRoom(id: Int): Boolean = dbQuery {
        Rooms.deleteWhere { Rooms.id eq id } > 0
    }

    override suspend fun deleteByBase(baseId: Int): Boolean = dbQuery {
        Rooms.deleteWhere { Rooms.baseId eq baseId } > 0
    }

    override suspend fun deleteByAccount(accId: Int): Boolean = dbQuery {
        val bases = DAOBaseImpl().basesByAccount(accId)
        val base_ids = mutableListOf<Int>()
        for (base in bases) base_ids.add(base.id)
        Rooms.deleteWhere { Rooms.baseId inList base_ids } > 0
    }
}

class DAOGearImpl : DAOGear {
    private fun resultRowToGear(row: ResultRow) = Gear(
        id = row[Equipment.id],
        roomId = row[Equipment.roomId],
        type = row[Equipment.type],
        brand = row[Equipment.brand],
        amount = row[Equipment.amount],
    )
    private fun resultRowToRoom(row: ResultRow) = Room(
        id = row[Rooms.id],
        baseId = row[Rooms.baseId],
        name = row[Rooms.name],
        type = row[Rooms.type],
        area = row[Rooms.area],
        cost = row[Rooms.cost],
    )

    override suspend fun gearByRoom(roomId: Int): List<Gear> = dbQuery {
        Equipment.select { Equipment.roomId eq roomId }.map(::resultRowToGear)
    }

    override suspend fun gear(id: Int): Gear? = dbQuery {
        Equipment.select { Equipment.id eq id }.map(::resultRowToGear).singleOrNull()
    }

    override suspend fun addGear(roomId: Int, type: String, brand: String, amount: Int): Gear? = dbQuery {
        val insertStatement = Equipment.insert {
            it[Equipment.roomId] = roomId
            it[Equipment.type] = type
            it[Equipment.brand] = brand
            it[Equipment.amount] = amount
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToGear)
    }

    override suspend fun deleteGear(id: Int): Boolean = dbQuery {
        Equipment.deleteWhere { Equipment.id eq id } > 0
    }

    override suspend fun deleteByRoom(roomId: Int): Boolean = dbQuery {
        Equipment.deleteWhere { Equipment.roomId eq roomId } > 0
    }

    override suspend fun deleteByAccount(accId: Int): Boolean = dbQuery {
        val bases = DAOBaseImpl().basesByAccount(accId)
        val base_ids = mutableListOf<Int>()
        for (base in bases) base_ids.add(base.id)
        val rooms = Rooms.select { Rooms.baseId inList base_ids }.map(::resultRowToRoom)
        val room_ids = mutableListOf<Int>()
        for (room in rooms) room_ids.add(room.id)
        Equipment.deleteWhere { Equipment.roomId inList room_ids } > 0
    }
}

class DAORehearsalImpl : DAORehearsal {
    private fun resultRowToRehearsal(row: ResultRow) = Rehearsal(
        id = row[Rehearsals.id],
        musicianId = row[Rehearsals.musicianId],
        roomId = row[Rehearsals.roomId],
        time = row[Rehearsals.time],
    )
    private fun resultRowToRoom(row: ResultRow) = Room(
        id = row[Rooms.id],
        baseId = row[Rooms.baseId],
        name = row[Rooms.name],
        type = row[Rooms.type],
        area = row[Rooms.area],
        cost = row[Rooms.cost],
    )

    override suspend fun rehsByBase(baseId: Int): List<Rehearsal> = dbQuery {
        val rooms = Rooms.select { Rooms.baseId eq baseId }.map(::resultRowToRehearsal)
        val room_ids = mutableListOf<Int>()
        for (room in rooms) room_ids.add(room.id)
        Rehearsals.select { Rehearsals.roomId inList room_ids }.map(::resultRowToRehearsal)
    }

    override suspend fun rehsByAccount(accId: Int): List<Rehearsal> = dbQuery {
        Rehearsals.select { Rehearsals.musicianId eq accId }.map(::resultRowToRehearsal)
    }

    override suspend fun rehearsal(id: Int): Rehearsal? = dbQuery {
        Rehearsals.select { Rehearsals.id eq id }.map(::resultRowToRehearsal).singleOrNull()
    }

    override suspend fun rehearsalByTime(roomId: Int, time: String): Rehearsal? = dbQuery {
        Rehearsals.select { (Rehearsals.roomId eq roomId) and
                (Rehearsals.time eq time) }.map(::resultRowToRehearsal).singleOrNull()
    }

    override suspend fun addRehearsal(musicianId: Int, roomId: Int, time: String): Rehearsal? = dbQuery {
        val insertStatement = Rehearsals.insert {
            it[Rehearsals.musicianId] = musicianId
            it[Rehearsals.roomId] = roomId
            it[Rehearsals.time] = time
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToRehearsal)
    }

    override suspend fun deleteRehearsal(id: Int): Boolean = dbQuery {
        Rehearsals.deleteWhere { Rehearsals.id eq id } > 0
    }

    override suspend fun deleteByBase(baseId: Int): Boolean = dbQuery {
        val rooms = Rooms.select { Rooms.baseId eq baseId }.map(::resultRowToRoom)
        val room_ids = mutableListOf<Int>()
        for (room in rooms) room_ids.add(room.id)
        Rehearsals.deleteWhere { Rehearsals.roomId inList room_ids } > 0
    }

    override suspend fun deleteByAccount(accId: Int): Boolean = dbQuery {
        Rehearsals.deleteWhere { Rehearsals.musicianId eq accId } > 0
    }
}