package com.example.src

import java.sql.Connection

class RehearsalBase
{
    var id: Int = -1
    var name: String? = null
    var address: String? = null
    var phone: String? = null
    var mail: String? = null
    var ownerId: Int = -1
}

class RehBaseRepository(val connect: Connection?)
{
    private val bases = PostgresAccess(connect).selectBases()

    fun saveBase(base: RehearsalBase) {
        if (base.id == -1)
            base.id = bases.size + 1
        var exists: Boolean = false
        var ind = 0
        for (b in bases) {
            if (b.id == base.id || b.name == base.name) {
                exists = true
                base.id = b.id  // если имена одинаковые, но id уже был присвоен новый
                break
            }
            ++ind
        }
        if (exists) {
            //println("updating base...")
            bases[ind] = base
            PostgresAccess(connect).update(base)
        }
        else {
            //println("inserting base...")
            bases.add(base)
            PostgresAccess(connect).insert(base)
        }
    }
    fun deleteBase(baseId: Int) {
        //println("deleting base...")
        for (ind in bases.size - 1 downTo 0) {
            if (bases[ind].id == baseId) {
                bases.removeAt(ind)
                break
            }
        }
        PostgresAccess(connect).deleteBase(baseId)
    }
    fun delByAcc(accId: Int) {
        //println("deleting bases by acc...")
        for (ind in bases.size - 1 downTo 0) {
            if (bases[ind].ownerId == accId) {
                bases.removeAt(ind)
            }
        }
        PostgresAccess(connect).deleteBasesByAcc(accId)
    }
    fun getBase(baseId: Int): RehearsalBase {
        //println("selecting base...")
        return PostgresAccess(connect).selectBase(baseId)
    }
    fun getAllBases(): MutableList<RehearsalBase> {
        //println("selecting all bases...")
        return PostgresAccess(connect).selectAllBases()
    }
    fun getBasesByAcc(accId: Int): MutableList<RehearsalBase> {
        return PostgresAccess(connect).selectBasesByAcc(accId)
    }
}

class RehBaseActs(val connect: Connection?)
{
    private val rep = RehBaseRepository(connect)

    fun save(base: RehearsalBase, room: Room, eq: Equipment) {
        rep.saveBase(base)
        if (room.name != null)
        {
            room.baseId = base.id
            RoomActs(connect).save(room, eq)
        }
    }
    fun delete(baseId: Int) {
        RoomActs(connect).delByBase(baseId)
        rep.deleteBase(baseId)
    }
    fun delByAcc(accId: Int) {
        RoomActs(connect).delByAcc(accId)
        rep.delByAcc(accId)
    }
    fun getBase(baseId: Int): RehearsalBase {
        return rep.getBase(baseId)
    }
    fun allBases(): MutableList<RehearsalBase> {
        return rep.getAllBases()
    }
    fun basesByAcc(accId: Int): MutableList<RehearsalBase> {
        return rep.getBasesByAcc(accId)
    }
}