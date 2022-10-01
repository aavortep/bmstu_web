package com.example.src

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import java.io.File
import java.sql.Connection
import java.sql.Time


/*fun test_business(connect: Connection?) {
    TestBusiness(connect).createAcc()
    TestBusiness(connect).showBases()
    TestBusiness(connect).getBase()
    TestBusiness(connect).bookReh()
    TestBusiness(connect).checkReh()
    TestBusiness(connect).cancelReh()
    TestBusiness(connect).regBase()
    TestBusiness(connect).allRehs()
    TestBusiness(connect).delRoom()
    TestBusiness(connect).delBase()
    TestBusiness(connect).delAcc()
}

fun test_access(connect: Connection?) {
    val acc = Account()
    acc.id = 1
    acc.fio = "Petrova A A"
    acc.phone = "89169885354"
    acc.mail = "nura.alexevna@yandex.ru"
    acc.password = "LinkinPark20"
    TestAccess(connect).createAcc(acc)
    acc.id = 2
    acc.fio = "Hilkevich D A"
    acc.phone = "89876543210"
    acc.mail = "qwerty@mail.ru"
    acc.password = "1234"
    TestAccess(connect).createAcc(acc)

    val base = RehearsalBase()
    val room = Room()
    base.id = 1
    base.name = "Highgain Studio"
    base.address = "Volgogradsky prospekt"
    base.phone = "89123456789"
    base.mail = "abcd@gmail.com"
    base.ownerId = 1
    room.id = 1
    room.name = "Yellow"
    room.area = 23
    room.cost = 1500
    room.type = "band"
    room.baseId = 1
    TestAccess(connect).regBase(base, room)
    room.id = 2
    room.name = "Black"
    room.area = 35
    room.cost = 1500
    room.type = "band"
    room.baseId = 1
    TestAccess(connect).regBase(base, room)

    TestAccess(connect).showBases()
    TestAccess(connect).getBase(1)

    val reh = Rehearsal()
    reh.id = 1
    reh.musicianId = 1
    reh.roomId = 1
    reh.time = Time(12, 0, 0)
    TestAccess(connect).bookReh(reh)
    TestAccess(connect).checkReh(1)
    reh.id = 2
    reh.musicianId = 2
    reh.roomId = 2
    reh.time = Time(17, 0, 0)
    TestAccess(connect).bookReh(reh)
    TestAccess(connect).allRehs(1)
    TestAccess(connect).cancelReh(2)

    TestAccess(connect).delAcc(1)
    TestAccess(connect).delAcc(2)
}*/

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 1000.0, 600.0)
        stage.title = "Welcome"
        stage.scene = scene
        stage.show()
    }
    fun sign_in() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("sign-in.fxml"))
        val scene = Scene(fxmlLoader.load(), 1000.0, 600.0)
        val stage = Stage()
        stage.title = "Sign in"
        stage.scene = scene
        stage.show()
    }
    fun sign_up() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("sign-up.fxml"))
        val scene = Scene(fxmlLoader.load(), 1000.0, 600.0)
        val stage = Stage()
        stage.title = "Sign up"
        stage.scene = scene
        stage.show()
    }
    fun main_page(acc: Account) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("main-page.fxml"))
        val rooms = Actions().allRooms()
        var scrollText = ""
        for (room in rooms) {
            scrollText += room.id.toString()
            scrollText += ". "
            scrollText += room.name
            scrollText += " ("
            scrollText += room.cost.toString()
            scrollText += " rub)\n"
        }
        val root = fxmlLoader.load<Parent>()
        val controller = fxmlLoader.getController<MainPageController>()
        controller.showRooms(Label(scrollText))
        controller.reh.musicianId = acc.id
        val scene = Scene(root, 1000.0, 600.0)
        val stage = Stage()
        stage.title = "Main page"
        stage.scene = scene
        stage.show()
    }
    fun owner_page(ownerId: Int) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("owner-page.fxml"))
        val root = fxmlLoader.load<Parent>()
        val controller = fxmlLoader.getController<OwnerController>()
        controller.base.ownerId = ownerId
        val scene = Scene(root, 1000.0, 600.0)
        val stage = Stage()
        stage.title = "Owner page"
        stage.scene = scene
        stage.show()
    }
    fun booked_rehs(accId: Int) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("booked-rehs.fxml"))
        val root = fxmlLoader.load<Parent>()
        val controller = fxmlLoader.getController<BookedRehsController>()
        val rehs = Actions().bookedRehs(accId)
        val rooms = Actions().allRooms()
        var scrollText = ""
        for (reh in rehs) {
            scrollText += rooms[reh.roomId - 1].name
            scrollText += " - "
            scrollText += reh.time
            scrollText += " ("
            scrollText += rooms[reh.roomId - 1].cost
            scrollText += " rub)\n"
        }
        controller.showRehs(Label(scrollText))
        val scene = Scene(root, 1000.0, 600.0)
        val stage = Stage()
        stage.title = "Booked rehearsals"
        stage.scene = scene
        stage.show()
    }
}

class Actions
{
    private val connect = PostgresAccess(null).connect()

    fun findAcc(mail: String, password: String): Account {
        File("log.txt").appendText("Authorisation attempt " +
                "with email: $mail\n")
        return AccActs(connect).findAcc(mail, password)
    }
    fun saveAcc(acc: Account): Int {
        File("log.txt").appendText("Account registration: " +
                "${acc.mail} ${acc.fio}\n")
        return AccActs(connect).save(acc)
    }
    fun allRooms(): MutableList<Room> {
        File("log.txt").appendText("All rooms request\n")
        return PostgresAccess(connect).selectAllRooms()
    }
    fun bookReh(reh: Rehearsal) {
        MusicianActs(connect).bookReh(reh)
        File("log.txt").appendText("Rehearsal booking: " +
                "musician ID - ${reh.musicianId}, " +
                "room ID - ${reh.roomId}, time - ${reh.time}\n")
    }
    fun delAcc(accId: Int) {
        AccActs(connect).delete(accId)
        File("log.txt").appendText("User $accId deleted his account\n")
    }
    fun saveBase(base: RehearsalBase, room: Room, eq: Equipment) {
        OwnerActs(connect).saveBase(base, room, eq)
        File("log.txt").appendText("Base registration or changing: " +
                "${base.name} (owner ID: ${base.ownerId})\n")
    }
    fun bookedRehs(accId: Int): MutableList<Rehearsal> {
        File("log.txt").appendText("Booked rehearsals request\n")
        return RehActs(connect).rehsByAcc(accId)
    }
}

fun main() {
    //val connect = PostgresAccess(null).connect()
    Application.launch(HelloApplication::class.java)

    //TUI(connect).welcome()
}