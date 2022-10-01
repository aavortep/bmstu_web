package com.example.src

import com.dlsc.formsfx.model.structure.IntegerField
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import java.sql.Time

class WelcomeController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
    }
    @FXML
    private fun onSignInButtonClick() {
        HelloApplication().sign_in()
    }
    @FXML
    private fun onSignUpButtonClick() {
        HelloApplication().sign_up()
    }
}

class SignInController
{
    @FXML
    private lateinit var passwordField: PasswordField
    @FXML
    private lateinit var mailField: TextField
    @FXML
    private lateinit var error: Label

    @FXML
    private fun onSubmitButtonClick() {
        val mail: String = mailField.text
        val password: String = passwordField.text
        if (mail == "" || password == "") {
            error.text = "Please, fill all the gaps"
            return
        }
        val acc = Actions().findAcc(mail, password)
        if (acc.id == -1) {
            error.text = "Invalid email or password. Try again"
            return
        }
        HelloApplication().main_page(acc)
    }
}

class SignUpController
{
    @FXML
    private lateinit var passwordField: PasswordField
    @FXML
    private lateinit var mailField: TextField
    @FXML
    private lateinit var fioField: TextField
    @FXML
    private lateinit var phoneField: TextField
    @FXML
    private lateinit var error: Label

    @FXML
    private fun onSubmitButtonClick() {
        val acc = Account()
        acc.fio = fioField.text
        acc.phone = phoneField.text
        acc.mail = mailField.text
        acc.password = passwordField.text
        if (acc.mail == "" || acc.password == "" || acc.fio == "" || acc.phone == "") {
            error.text = "Please, fill all the gaps"
            return
        }
        acc.id = Actions().saveAcc(acc)
        HelloApplication().main_page(acc)
    }
}

class MainPageController
{
    @FXML
    private lateinit var roomIdField: TextField
    @FXML
    private lateinit var hourField: TextField
    @FXML
    private lateinit var scrollPane: ScrollPane
    @FXML
    private lateinit var error: Label
    val reh = Rehearsal()

    fun showRooms(content: Label) {
        scrollPane.content = content
        scrollPane.prefViewportHeight = 150.0
        scrollPane.prefViewportWidth = 200.0
    }
    @FXML
    private fun onBookButtonClick() {
        val hour: Int
        try {
            reh.roomId = roomIdField.text.toInt()
            hour = hourField.text.toInt()
        }
        catch (e: NumberFormatException) {
            error.text = "Invalid input"
            return
        }
        reh.time = Time(hour, 0, 0)
        Actions().bookReh(reh)
        error.text = "Rehearsal was booked!"
    }
    @FXML
    private fun onShowButtonClick() {
        HelloApplication().booked_rehs(reh.musicianId)
    }
    @FXML
    private fun onRegBaseButtonClick() {
        HelloApplication().owner_page(reh.musicianId)
    }
    @FXML
    private fun onDelButtonClick() {
        Actions().delAcc(reh.musicianId)
        HelloApplication().sign_up()
    }
}

class OwnerController
{
    @FXML
    private lateinit var baseNameField: TextField
    @FXML
    private lateinit var addressField: TextField
    @FXML
    private lateinit var phoneField: TextField
    @FXML
    private lateinit var mailField: TextField
    @FXML
    private lateinit var nameField: TextField
    @FXML
    private lateinit var typeField: TextField
    @FXML
    private lateinit var areaField: TextField
    @FXML
    private lateinit var costField: TextField
    @FXML
    private lateinit var equipTypeField: TextField
    @FXML
    private lateinit var brandField: TextField
    @FXML
    private lateinit var amountField: TextField
    @FXML
    private lateinit var error: Label
    val base = RehearsalBase()

    @FXML
    private fun onSubmitButtonClick() {
        base.name = baseNameField.text
        base.address = addressField.text
        base.phone = phoneField.text
        base.mail = mailField.text
        if (base.name == "" || base.address == "" || base.mail == "" || base.phone == "") {
            error.text = "Please, fill all the gaps"
            return
        }

        val room = Room()
        room.name = nameField.text
        room.type = typeField.text
        try {
            room.area = areaField.text.toInt()
            room.cost = costField.text.toInt()
        }
        catch (e: NumberFormatException) {
            error.text = "Invalid room area or cost"
            return
        }

        val equip = Equipment()
        equip.type = equipTypeField.text
        equip.brand = brandField.text
        try {
            equip.amount = amountField.text.toInt()
        }
        catch (e: java.lang.NumberFormatException) {
            error.text = "Invalid amount of equipment"
            return
        }

        Actions().saveBase(base, room, equip)
        error.text = "Rehearsal base saved!"
    }
}

class BookedRehsController
{
    @FXML
    private lateinit var scrollPane: ScrollPane

    fun showRehs(content: Label) {
        scrollPane.content = content
        scrollPane.prefViewportHeight = 150.0
        scrollPane.prefViewportWidth = 200.0
    }
}