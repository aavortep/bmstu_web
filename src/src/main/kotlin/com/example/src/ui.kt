package com.example.src

import java.sql.Connection
import java.sql.Time

/*class CheckData()
{
    private fun is_empty(str: String?): Boolean {
        return (str == null || str == "\n" || str == "")
    }
    fun read_int(): Int {
        var option: Int?
        try {
            option = readLine()?.toInt()
        }
        catch (e: NumberFormatException) {
            println("Invalid option")
            option = -1
        }
        if (option == null) {
            println("Invalid input")
            option = -1
        }
        return option
    }
    fun read_str(): String? {
        var tmp = readLine()
        if (is_empty(tmp)) {
            println("Invalid input")
            tmp = null
        }
        return tmp
    }
    fun conv_int(str: String): Int? {
        var num: Int?
        try {
            num = str.toInt()
        }
        catch (e: NumberFormatException) {
            println("Invalid input")
            num = null
        }
        return num
    }
}

class TUI(val connect: Connection?)
{
    private val pres = CheckData()
    private var option: Int? = -1
    private val welcome_menu = "0. Exit\n 1. Sign in\n 2. Sign up"
    private val musician_menu = "0. Exit\n 3. Book rehearsal\n" +
            "4. Show booked rehearsals\n 5. Cancel rehearsal\n 6. To owner mode\n" +
            "7. Account settings\n 8. Delete account\n 9. Log out"
    private val owner_menu = "0. Exit\n 10. All rehearsals\n 11. Register base\n" +
            "12. Base settings\n 13. Delete base\n 14. To musician mode"

    fun welcome() {
        println("Welcome to HearBase!\n")
        while (option != 0) {
            println(welcome_menu)
            print("> ")
            option = pres.read_int()

            when (option) {
                0 -> break
                1 -> {
                    println("LOGGING IN\n")
                    print("Mail: ")
                    var tmp = pres.read_str() ?: continue
                    val mail: String = tmp
                    print("Password: ")
                    tmp = pres.read_str() ?: continue
                    val password: String = tmp

                    val acc = AccActs(connect).findAcc(mail, password)
                    if (acc.id == -1) {
                        println("Invalid email or password. Try again")
                        continue
                    }
                    println("LOGGED IN SUCCESSFULLY")
                    option = musician(acc)
                }
                2 -> {
                    println("CREATING NEW ACCOUNT\n")
                    val acc = Account()
                    print("FIO: ")
                    var tmp = pres.read_str() ?: continue
                    acc.fio = tmp
                    print("Phone: ")
                    tmp = pres.read_str() ?: continue
                    acc.phone = tmp
                    print("Mail: ")
                    tmp = pres.read_str() ?: continue
                    acc.mail = tmp
                    print("Password: ")
                    tmp = pres.read_str() ?: continue
                    acc.password = tmp

                    acc.id = AccActs(connect).save(acc)
                    println("ACC CREATED SUCCESSFULLY")
                    option = musician(acc)
                }
                else -> println("Invalid option")
            }
        }
    }
    private fun musician(acc: Account): Int? {
        while (option != 0) {
            println(musician_menu)
            print("> ")
            option = pres.read_int()

            when (option) {
                0 -> break
                3 -> {
                    val reh = Rehearsal()
                    val rooms = PostgresAccess(connect).selectAllRooms()
                    println("ALL ROOMS:")
                    for (i in 0 until rooms.size) {
                        println("${rooms[i].id}. ${rooms[i].name} (${rooms[i].type})")
                    }
                    print("Input ID of the room, you want to book: ")
                    var tmp = pres.read_str() ?: continue
                    reh.roomId = pres.conv_int(tmp) ?: continue
                    print("Rehearsal hour: ")
                    tmp = pres.read_str() ?: continue
                    val hour = pres.conv_int(tmp) ?: continue
                    reh.time = Time(hour, 0, 0)
                    reh.musicianId = acc.id

                    MusicianActs(connect).bookReh(reh)
                    println("REHEARSAL BOOKED SUCCESSFULLY")
                }
                4 -> {
                    val rehs = RehActs(connect).rehsByAcc(acc.id)
                    val rooms = PostgresAccess(connect).selectAllRooms()
                    println("YOUR BOOKED REHEARSALS:")
                    for (i in 0 until rehs.size) {
                        println("${rooms[rehs[i].roomId-1].name} - ${rehs[i].time}")
                    }
                }
                5 -> {
                    val rehs = RehActs(connect).rehsByAcc(acc.id)
                    val rooms = PostgresAccess(connect).selectAllRooms()
                    var rehId: Int
                    println("YOUR BOOKED REHEARSALS:")
                    for (i in 0 until rehs.size) {
                        println("${rehs[i].id}. ${rooms[rehs[i].roomId-1].name} - " +
                                "${rehs[i].time}")
                    }
                    print("Choose rehearsal, you want to cancel: ")
                    val tmp = pres.read_str() ?: continue
                    rehId = pres.conv_int(tmp) ?: continue

                    MusicianActs(connect).cancelReh(rehId)
                    println("REHEARSAL CANCELED SUCCESSFULLY")
                }
                6 -> option = owner(acc)
                7 -> {
                    val accs = PostgresAccess(connect).selectAllAccs()
                    println("GOING TO ACC SETTINGS")
                    val cur_acc = accs[acc.id - 1]
                    println("Current state: ${cur_acc.fio} ${cur_acc.mail} " +
                            "${cur_acc.phone} ${cur_acc.password}")
                    print("Want to change? (Y/n) ")
                    if (readLine() == "n")
                        continue
                    print("New phone: ")
                    var tmp = pres.read_str() ?: continue
                    cur_acc.phone = tmp
                    print("New mail: ")
                    tmp = pres.read_str() ?: continue
                    cur_acc.mail = tmp
                    print("New password: ")
                    tmp = pres.read_str() ?: continue
                    cur_acc.password = tmp

                    AccActs(connect).save(cur_acc)
                    println("ACC CHANGED SUCCESSFULLY")
                }
                8 -> {
                    AccActs(connect).delete(acc.id)
                    println("ACC DELETED SUCCESSFULLY")
                    welcome()
                }
                9 -> welcome()
                else -> println("Invalid option")
            }
        }

        return option
    }
    private fun owner(acc: Account): Int? {
        while (option != 0) {
            println(owner_menu)
            print("> ")
            option = pres.read_int()

            when (option) {
                0 -> break
                10 -> {
                    val bases = RehBaseActs(connect).basesByAcc(acc.id)
                    var baseId: Int
                    println("YOUR BASES:")
                    for (i in 0 until bases.size) {
                        println("${bases[i].id}. ${bases[i].name}")
                    }
                    print("Choose base: ")
                    val tmp = pres.read_str() ?: continue
                    baseId = pres.conv_int(tmp) ?: continue

                    val rehs = OwnerActs(connect).allRehs(baseId)
                    val rooms = PostgresAccess(connect).selectAllRooms()
                    val accs = PostgresAccess(connect).selectAllAccs()
                    println("BOOKED REHEARSALS ON THIS BASE:")
                    for (i in 0 until rehs.size) {
                        println("${rooms[rehs[i].roomId-1].name} ${rehs[i].time} " +
                                "${accs[rehs[i].musicianId-1].fio}")
                    }
                }
                11 -> {
                    println("ADDING NEW BASE")
                    val base = RehearsalBase()
                    print("Name: ")
                    var tmp = pres.read_str() ?: continue
                    base.name = tmp
                    print("Address: ")
                    tmp = pres.read_str() ?: continue
                    base.address = tmp
                    print("Phone: ")
                    tmp = pres.read_str() ?: continue
                    base.phone = tmp
                    print("Mail: ")
                    tmp = pres.read_str() ?: continue
                    base.mail = tmp
                    base.ownerId = acc.id

                    print("How many rooms? ")
                    tmp = pres.read_str() ?: continue
                    val cnt: Int = pres.conv_int(tmp) ?: continue
                    for (i in 1..cnt) {
                        println("ADDING ROOM $i")
                        val room = Room()
                        print("Name: ")
                        tmp = pres.read_str() ?: continue
                        room.name = tmp
                        print("Type: ")
                        tmp = pres.read_str() ?: continue
                        room.type = tmp
                        print("Area: ")
                        tmp = pres.read_str() ?: continue
                        room.area = pres.conv_int(tmp) ?: continue
                        print("Cost: ")
                        tmp = pres.read_str() ?: continue
                        room.cost = pres.conv_int(tmp) ?: continue

                        OwnerActs(connect).saveBase(base, room)
                    }

                    println("BASE REGISTERED SUCCESSFULLY")
                }
                12 -> {
                    println("BASES SETTINGS")

                    val bases = RehBaseActs(connect).basesByAcc(acc.id)
                    var baseId: Int
                    println("YOUR BASES:")
                    for (i in 0 until bases.size) {
                        println("${bases[i].id}. ${bases[i].name}")
                    }
                    print("Choose base: ")
                    var tmp = pres.read_str() ?: continue
                    baseId = pres.conv_int(tmp) ?: continue

                    println("GOING TO BASE $baseId SETTINGS")
                    val base = RehBaseActs(connect).getBase(baseId)
                    val room = Room()
                    println("Current state: ${base.name} ${base.address} " +
                            "${base.phone} ${base.mail}")
                    print("Want to change? (Y/n) ")
                    if (readLine() == "n")
                        continue
                    print("New name: ")
                    tmp = pres.read_str() ?: continue
                    base.name = tmp
                    print("New address: ")
                    tmp = pres.read_str() ?: continue
                    base.address = tmp
                    print("New phone: ")
                    tmp = pres.read_str() ?: continue
                    base.phone = tmp
                    print("New mail: ")
                    tmp = pres.read_str() ?: continue
                    base.mail = tmp

                    OwnerActs(connect).saveBase(base, room)
                    println("BASE CHANGED SUCCESSFULLY")
                }
                13 -> {
                    val bases = RehBaseActs(connect).basesByAcc(acc.id)
                    var baseId: Int
                    println("YOUR BASES:")
                    for (i in 0 until bases.size) {
                        println("${bases[i].id}. ${bases[i].name}")
                    }
                    print("Choose base, you want to delete: ")
                    val tmp = pres.read_str() ?: continue
                    baseId = pres.conv_int(tmp) ?: continue

                    OwnerActs(connect).delBase(baseId)
                    println("BASE $baseId DELETED SUCCESSFULLY")
                }
                14 -> option = musician(acc)
                else -> println("Invalid option")
            }
        }

        return option
    }
}*/
