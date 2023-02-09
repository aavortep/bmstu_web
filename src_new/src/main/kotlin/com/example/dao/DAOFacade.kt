package com.example.dao

import com.example.models.*

interface DAOAccount {
    suspend fun allAccounts(): List<Account>
    suspend fun account(id: Int): Account?
    suspend fun login(mail: String, password: String): Account?
    suspend fun addAccount(fio: String, phone: String, mail: String,
                           password: String, type: String): Account?
    suspend fun deleteAccount(id: Int): Boolean
}

interface DAOBase {
    suspend fun allBases(): List<RehearsalBase>
    suspend fun basesByAccount(accId: Int): List<RehearsalBase>
    suspend fun base(id: Int): RehearsalBase?
    suspend fun baseByAddress(address: String): RehearsalBase?
    suspend fun addBase(ownerId: Int, name: String, address: String, phone: String,
                        mail: String): RehearsalBase?
    suspend fun deleteBase(id: Int): Boolean
    suspend fun deleteByAccount(accId: Int): Boolean
}

interface DAORoom {
    suspend fun allRooms(): List<Room>
    suspend fun room(id: Int): Room?
    suspend fun roomByBase(baseId: Int, name: String): Room?
    suspend fun addRoom(baseId: Int, name: String, type: String, area: Int, cost: Int): Room?
    suspend fun deleteRoom(id: Int): Boolean
    suspend fun deleteByBase(baseId: Int): Boolean
    suspend fun deleteByAccount(accId: Int): Boolean
}

interface DAOGear {
    suspend fun gearByRoom(roomId: Int): List<Gear>
    suspend fun gear(id: Int): Gear?
    suspend fun addGear(roomId: Int, type: String, brand: String, amount: Int): Gear?
    suspend fun deleteGear(id: Int): Boolean
    suspend fun deleteByRoom(roomId: Int): Boolean
    suspend fun deleteByAccount(accId: Int): Boolean
}

interface DAORehearsal {
    suspend fun rehsByBase(baseId: Int): List<Rehearsal>
    suspend fun rehsByAccount(accId: Int): List<Rehearsal>
    suspend fun rehearsal(id: Int): Rehearsal?
    suspend fun rehearsalByTime(roomId: Int, time: String): Rehearsal?
    suspend fun addRehearsal(musicianId: Int, roomId: Int, time: String): Rehearsal?
    suspend fun deleteRehearsal(id: Int): Boolean
    suspend fun deleteByBase(baseId: Int): Boolean
    suspend fun deleteByAccount(accId: Int): Boolean
}