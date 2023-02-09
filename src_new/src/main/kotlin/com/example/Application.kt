package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.DatabaseFactory
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        authentication {
            jwt("auth-jwt") {
                realm = "hearbase"
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withAudience("http://0.0.0.0:8080/api/v1/logout",
                            "http://0.0.0.0:8080/api/v1/users",
                            "http://0.0.0.0:8080/api/v1/bases/{baseId}",
                            "http://0.0.0.0:8080/api/v1/rooms",
                            "http://0.0.0.0:8080/api/v1/rehearsals/{rehId}")
                        .withIssuer("http://0.0.0.0:8080/api/v1/")
                        .build()
                )
                validate { credential ->
                    if ((credential.payload.getClaim("mail").asString() != "") and
                        (credential.payload.getClaim("password").asString() != "")) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
            }
        }
        configureRouting()
    }.start(wait = true)
}
