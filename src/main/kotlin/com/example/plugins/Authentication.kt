package com.example.plugins

import com.example.domain.model.EndPoints
import com.example.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun Application.configureAuth(){

    install(Authentication ){
        session<UserSession>("auth-session"){
            validate {
                session -> session
            }

            challenge{
                call.respondRedirect(EndPoints.Unauthorized.path)
            }
        }

    }

}