package com.example.src

import java.sql.Connection

class Room
{
    var id: Int = -1
    var name: String? = null
    var type: String? = null
    var area: Int = -1
    var cost: Int = -1
    var baseId: Int = -1
}

class RoomRepository(val connect: Connection?)
{
    private val rooms = PostgresAccess(connect).selectAllRooms()

    fun saveRoom(room: Room) {
        if (room.id == -1)
            room.id = rooms.size + 1
        var exists: Boolean = false
        var ind = 0
        for (r in rooms) {
            if (r.id == room.id || r.baseId == room.baseId) {
                exists = true
                room.id = r.id  // если комната та же, но id был уже присвоен новый
                break
            }
            ++ind
        }
        if (exists) {
            //println("updating room...")
            rooms[ind] = room
            PostgresAccess(connect).update(room)
        }
        else {
            //println("inserting room...")
            rooms.add(room)
            PostgresAccess(connect).insert(room)
        }
    }
    fun deleteRoom(roomId: Int) {
        //println("deleting room...")
        var ind = 0
        for (r in rooms) {
            if (r.id == roomId) {
                rooms.removeAt(ind)
                break
            }
            ++ind
        }
        PostgresAccess(connect).deleteRoom(roomId)
    }
    fun delByBase(baseId: Int) {
        //println("deleting rooms by base...")
        for (ind in rooms.size - 1 downTo 0) {
            if (rooms[ind].baseId == baseId) {
                rooms.removeAt(ind)
            }
        }
        PostgresAccess(connect).deleteRoomsByBase(baseId)
    }
    fun delByAcc(accId: Int) {
        //println("deleting rooms by acc...")
        if (rooms.isEmpty()) {
            //println("no rooms by acc $accId")
            return
        }
        val basesId = mutableListOf<Int>()
        val bases = PostgresAccess(connect).selectBases()
        for (b in bases){
            if (b.ownerId == accId)
                basesId.add(b.id)
        }
        for (i in 0..basesId.size) {
            for (ind in rooms.size - 1 downTo 0) {
                if (rooms[ind].baseId == basesId[i]) {
                    rooms.removeAt(ind)
                }
            }
        }
        PostgresAccess(connect).deleteRoomsByAcc(accId)
    }
}

class RoomActs(val connect: Connection?)
{
    private val rep = RoomRepository(connect)

    fun save(room: Room, eq: Equipment) {
        rep.saveRoom(room)
        if (eq.type != null)
        {
            eq.roomId = room.id
            EquipActs(connect).save(eq)
        }
    }
    fun delete(roomId: Int) {
        rep.deleteRoom(roomId)
    }
    fun delByBase(baseId: Int) {
        rep.delByBase(baseId)
    }
    fun delByAcc(accId: Int) {
        EquipActs(connect).delByAcc(accId)
        rep.delByAcc(accId)
    }
}