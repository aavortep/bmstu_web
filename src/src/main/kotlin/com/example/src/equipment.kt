package com.example.src

import java.sql.Connection

class Equipment
{
    var id: Int = -1
    var roomId: Int = -1
    var type: String? = null
    var brand: String? = null
    var amount: Int = 0
}

class EquipRepository(val connect: Connection?)
{
    private val equip = PostgresAccess(connect).selectAllEquip()

    fun saveEquip(eq: Equipment) {
        if (eq.id == -1)
            eq.id = equip.size + 1
        var exists: Boolean = false
        var ind = 0
        for (e in equip) {
            if (e.id == eq.id) {
                exists = true
                break
            }
            ++ind
        }
        if (exists) {
            equip[ind] = eq
            PostgresAccess(connect).update(eq)
        }
        else {
            equip.add(eq)
            PostgresAccess(connect).insert(eq)
        }
    }
    fun deleteEquip(equipId: Int) {
        var ind = 0
        for (e in equip) {
            if (e.id == equipId) {
                equip.removeAt(ind)
                break
            }
            ++ind
        }
        PostgresAccess(connect).deleteEquip(equipId)
    }
    fun delByRoom(roomId: Int) {
        for (ind in equip.size - 1 downTo 0) {
            if (equip[ind].roomId == roomId) {
                equip.removeAt(ind)
            }
        }
        PostgresAccess(connect).deleteEquipByRoom(roomId)
    }
    fun delByAcc(accId: Int) {
        PostgresAccess(connect).deleteEquipByAcc(accId)
    }
}

class EquipActs(val connect: Connection?)
{
    private val rep = EquipRepository(connect)

    fun save(equip: Equipment) {
        rep.saveEquip(equip)
    }
    fun delete(equipId: Int) {
        rep.deleteEquip(equipId)
    }
    fun delByRoom(roomId: Int) {
        rep.delByRoom(roomId)
    }
    fun delByAcc(accId: Int) {
        rep.delByAcc(accId)
    }
}