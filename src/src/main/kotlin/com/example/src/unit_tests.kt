package com.example.src

import java.sql.Connection
import java.sql.Time

/*class TestBusiness(val connect: Connection?)
{
    private var acc = Account()
    private var base = RehearsalBase()
    private var room = Room()
    private var reh = Rehearsal()

    fun createAcc() {
        acc.id = 1
        acc.fio = "Петрова А А"
        acc.phone = "89169885354"
        acc.mail = "nura.alexevna@yandex.ru"
        acc.password = "LinkinPark20"
        MusicianActs(connect).save(acc)
    }
    fun delAcc() {
        MusicianActs(connect).delete(1)
    }
    fun showBases() {
        RehBaseActs(connect).allBases()
    }
    fun getBase() {
        RehBaseActs(connect).getBase(3)
    }
    fun bookReh() {
        reh.id = 1
        reh.musicianId = 1
        reh.roomId = 1
        reh.time = Time(12, 0, 0)
        MusicianActs(connect).bookReh(reh)
    }
    fun cancelReh() {
        MusicianActs(connect).cancelReh(1)
    }
    fun checkReh() {
        MusicianActs(connect).checkReh(1)
    }
    fun regBase() {
        base.id = 1
        base.name = "Highgain Studio"
        base.address = "Волгоградский проспект"
        base.phone = "89123456789"
        base.mail = "abcd@gmail.com"
        base.ownerId = 1

        room.id = 1
        room.name = "Yellow"
        room.area = 23
        room.cost = 1500
        room.type = "band"
        room.baseId = 1

        OwnerActs(connect).saveBase(base, room)
    }
    fun delBase() {
        OwnerActs(connect).delBase(1)
    }
    fun allRehs() {
        OwnerActs(connect).allRehs(1)
    }
    fun delRoom() {
        OwnerActs(connect).delRoom(1)
    }
}

class TestAccess(var connect: Connection?)
{
    fun createAcc(acc: Account) {
        MusicianActs(connect).save(acc)
    }
    fun delAcc(accId: Int) {
        MusicianActs(connect).delete(accId)
    }
    fun showBases() {
        RehBaseActs(connect).allBases()
    }
    fun getBase(baseId: Int) {
        RehBaseActs(connect).getBase(baseId)
    }
    fun bookReh(reh: Rehearsal) {
        MusicianActs(connect).bookReh(reh)
    }
    fun cancelReh(rehId: Int) {
        MusicianActs(connect).cancelReh(rehId)
    }
    fun checkReh(rehId: Int) {
        MusicianActs(connect).checkReh(rehId)
    }
    fun regBase(base: RehearsalBase, room: Room) {
        OwnerActs(connect).saveBase(base, room)
    }
    fun delBase(baseId: Int) {
        OwnerActs(connect).delBase(baseId)
    }
    fun allRehs(baseId: Int) {
        OwnerActs(connect).allRehs(baseId)
    }
    fun delRoom(roomId: Int) {
        OwnerActs(connect).delRoom(roomId)
    }
}*/
