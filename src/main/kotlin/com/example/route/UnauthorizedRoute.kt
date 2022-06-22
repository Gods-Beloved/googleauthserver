package com.example.route

import com.example.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unauthorizedRoute(){

    get(EndPoints.Unauthorized.path) {

        call.respond(
            message = "Not Authorized",
            status = HttpStatusCode.Unauthorized
        )

    }

}