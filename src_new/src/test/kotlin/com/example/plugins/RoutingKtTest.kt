package com.example.plugins;

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.*

class RoutingKtTest {

    @Test
    fun testLogin() = testApplication {
        application {
            configureRouting()
        }
        client.post("/api/v1/login").apply {
            //
        }
    }
}