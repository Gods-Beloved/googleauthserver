package com.example.route

import com.example.domain.model.EndPoints
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUserRoute(
    app: Application,
    userDateSource: UserDataSource
) {

    authenticate("auth-server") {

        delete(EndPoints.DeleteUser.path) {

            val userSession = call.principal<UserSession>()

            if (userSession == null){
                app.log.info("INVALID SESSION")
                call.respondRedirect(EndPoints.Unauthorized.path)
            }else{
                try {

                }catch (e:Exception){

                }
            }

        }

    }


}