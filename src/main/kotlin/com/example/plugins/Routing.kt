package com.example.plugins

import com.example.route.authorizedRoute
import com.example.route.rootRoute
import com.example.route.tokenVerificationRoute
import com.example.route.unauthorizedRoute
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {

        rootRoute()
        tokenVerificationRoute(application)
        authorizedRoute()
        unauthorizedRoute()



        }

}
