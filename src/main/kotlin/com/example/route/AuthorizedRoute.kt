package com.example.route

import com.example.domain.model.ApiResponse
import com.example.domain.model.EndPoints
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.http.impl.auth.BasicScheme.authenticate

fun Routing.authorizedRoute(){

    authenticate("auth-session"){
        get(EndPoints.Authorized.path){

            call.respond(
                message = ApiResponse(success = true),
                status = HttpStatusCode.OK
            )

        }
    }


}