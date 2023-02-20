package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.util.*
import com.example.dao.DAOAccountImpl
import com.example.dao.DAOBaseImpl
import com.example.dao.DAORehearsalImpl
import com.example.dao.DAORoomImpl
import com.example.models.Account
import com.example.models.Rehearsal
import com.example.models.RehearsalBase
import com.example.models.Room
import com.google.gson.Gson
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {
        route("api/v1") {
            static {
                resource("doc/swagger.yaml", "doc/swagger.yaml")
                resource("doc", "doc/index.html")
            }
            get {
                call.respondText { "Welcome to HearBase!" }
            }
            post("/login") {
                val formParameters = call.receiveParameters()
                val mail = formParameters["mail"].toString()
                val password = formParameters["password"].toString()
                // receives user credentials sent as a JSON object and converts it
                // to an Account class object
                //val user = call.receive<Account>()
                val account = DAOAccountImpl().login(mail, password)
                if (account == null) call.response.status(HttpStatusCode.NotFound)
                else {
                    val token = JWT.create()
                        .withAudience("http://0.0.0.0:8080/api/v1/logout",
                            "http://0.0.0.0:8080/api/v1/users",
                            "http://0.0.0.0:8080/api/v1/bases/{baseId}",
                            "http://0.0.0.0:8080/api/v1/rooms",
                            "http://0.0.0.0:8080/api/v1/rehearsals/{rehId}")
                        .withIssuer("http://0.0.0.0:8080/api/v1/")
                        .withClaim("mail", mail)
                        .withClaim("password", password)
                        .sign(Algorithm.HMAC256("secret"))
                    // sends a token to a client as a JSON object
                    call.respond(hashMapOf("token" to token))

                    /*if (account != null) {
                        when (account.type) {
                            "musician" -> call.respondRedirect("/api/v1/rooms")
                            "owner" -> call.respondRedirect("/api/v1/users/${account.id}/bases/all")
                        }
                    }*/
                }
            }
            // register
            post("/users") {
                val formParameters = call.receiveParameters()
                val fio = formParameters["fio"].toString()
                val phone = formParameters["phone"].toString()
                val mail = formParameters["mail"].toString()
                val password = formParameters["password"].toString()
                val type = formParameters["type"].toString()
                var account = DAOAccountImpl().login(mail, password)
                if (account != null) call.response.status(HttpStatusCode.Conflict)  // user exists
                else {
                    account = DAOAccountImpl().addAccount(fio, phone, mail, password, type)

                    val token = JWT.create()
                        .withAudience("http://0.0.0.0:8080/api/v1/logout",
                            "http://0.0.0.0:8080/api/v1/users",
                            "http://0.0.0.0:8080/api/v1/bases/{baseId}",
                            "http://0.0.0.0:8080/api/v1/rooms",
                            "http://0.0.0.0:8080/api/v1/rehearsals/{rehId}")
                        .withIssuer("http://0.0.0.0:8080/api/v1/")
                        .withClaim("mail", mail)
                        .withClaim("password", password)
                        .sign(Algorithm.HMAC256("secret"))
                    // sends a token to a client as a JSON object
                    call.respond(hashMapOf("token" to token))

                    /*when (type) {
                        "musician" -> call.respondRedirect("/api/v1/rooms")
                        "owner" -> call.respondRedirect("/api/v1/users/${account?.id}/bases/all")
                        else -> call.response.status(HttpStatusCode.BadRequest)
                    }*/
                }
            }

            authenticate("auth-jwt") {
                get("/logout") {
                    call.respondRedirect("/api/v1")
                }

                route("users") {
                    delete("{userId}") {
                        val id = call.parameters.getOrFail<Int>("userId").toInt()
                        DAORehearsalImpl().deleteByAccount(id)
                        DAORoomImpl().deleteByAccount(id)
                        DAOBaseImpl().deleteByAccount(id)
                        DAOAccountImpl().deleteAccount(id)
                        call.respondRedirect("/api/v1")
                    }
                    get("{userId}/rehearsals") {
                        val id = call.parameters.getOrFail<Int>("userId").toInt()
                        val rehearsals = DAORehearsalImpl().rehsByAccount(id)
                        call.respondText { Gson().toJson(rehearsals) }
                    }
                    post("{userId}/rooms/{roomId}/rehearsals") {
                        val reh = call.receive<Rehearsal>()
                        val musicianId = call.parameters.getOrFail<Int>("userId").toInt()
                        val roomId = call.parameters.getOrFail<Int>("roomId").toInt()
                        val rehExists = DAORehearsalImpl().rehearsalByTime(roomId, reh.time!!)
                        if (rehExists == null) {
                            val rehearsal = DAORehearsalImpl().addRehearsal(musicianId, roomId, reh.time!!)
                            call.respondText { Gson().toJson(rehearsal) }
                        }
                        else call.response.status(HttpStatusCode.Conflict)  // reh exists
                    }
                    get("{userId}/bases/all") {
                        val principal = call.principal<JWTPrincipal>()
                        val mail = principal!!.payload.getClaim("mail").asString()
                        val id = call.parameters.getOrFail<Int>("userId").toInt()
                        val bases = DAOBaseImpl().basesByAccount(id)
                        call.respondText { "Logged in as $mail\n" + Gson().toJson(bases) }
                    }
                    post("{userId}/bases") {
                        val rehbase = call.receive<RehearsalBase>()
                        val ownerId = call.parameters.getOrFail<Int>("userId").toInt()
                        val baseExists = DAOBaseImpl().baseByAddress(rehbase.address!!)
                        if (baseExists == null) {
                            val base = DAOBaseImpl().addBase(ownerId, rehbase.name!!, rehbase.address!!,
                                rehbase.phone!!, rehbase.mail!!)
                            call.respondText { Gson().toJson(base) }
                        }
                        else call.response.status(HttpStatusCode.Conflict)  // base exists
                    }
                }

                route("bases") {
                    get("{baseId}/rehearsals") {
                        val baseId = call.parameters.getOrFail<Int>("baseId").toInt()
                        val rehs = DAORehearsalImpl().rehsByBase(baseId)
                        call.respondText { Gson().toJson(rehs) }
                    }
                    post("{baseId}/rooms") {
                        val room = call.receive<Room>()
                        val baseId = call.parameters.getOrFail<Int>("baseId").toInt()
                        val roomExists = DAORoomImpl().roomByBase(baseId, room.name!!)
                        if (roomExists == null) {
                            val res_room = DAORoomImpl().addRoom(baseId, room.name!!, room.type!!,
                                room.area, room.cost)
                            call.respondText { Gson().toJson(res_room) }
                        }
                        else call.response.status(HttpStatusCode.Conflict)  // room exists
                    }
                    delete("{baseId}") {
                        val baseId = call.parameters.getOrFail<Int>("baseId").toInt()
                        DAORehearsalImpl().deleteByBase(baseId)
                        DAORoomImpl().deleteByBase(baseId)
                        DAOBaseImpl().deleteBase(baseId)
                    }
                }

                route("rooms") {
                    get {
                        val principal = call.principal<JWTPrincipal>()
                        val mail = principal!!.payload.getClaim("mail").asString()
                        val rooms = DAORoomImpl().allRooms()
                        call.respondText { "Logged in as $mail\n" + Gson().toJson(rooms) }
                    }
                    get("{roomId}") {
                        val roomId = call.parameters.getOrFail<Int>("roomId").toInt()
                        val room = DAORoomImpl().room(roomId)
                        call.respondText { Gson().toJson(room) }
                    }
                }

                route("rehearsals") {
                    get("{rehId}") {
                        val rehId = call.parameters.getOrFail<Int>("rehId").toInt()
                        val reh = DAORehearsalImpl().rehearsal(rehId)
                        call.respondText { Gson().toJson(reh) }
                    }
                    delete("{rehId}") {
                        val rehId = call.parameters.getOrFail<Int>("rehId").toInt()
                        DAORehearsalImpl().deleteRehearsal(rehId)
                    }
                }
            }
        }
    }
}
