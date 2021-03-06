package com.example.route

import com.example.domain.model.ApiResponse
import com.example.domain.model.EndPoints
import com.example.domain.model.UserSession
import com.example.domain.model.UserUpdate
import com.example.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.updateUserRoute(
    app:Application,
    userDataSource: UserDataSource

){
    authenticate ("auth-session"){


        put(EndPoints.UpdateUserInfo.path){
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<UserUpdate>()

            if (userSession == null){
                call.respondRedirect(EndPoints.Unauthorized.path)
                app.log.info("INVALID SESSION")
            }else{
                try {

updateUserInfo(app = application, userId = userSession.id, userUpdate = userUpdate,userDataSource = userDataSource)
                }catch (e:Exception){
                    app.log.info("UPDATE USER INFO ERROR: $e")
                    call.respondRedirect(EndPoints.Unauthorized.path)

                }
            }

        }
    }



}
private suspend fun PipelineContext<Unit,ApplicationCall>.updateUserInfo(
    app: Application,
    userId:String,
    userUpdate: UserUpdate,
    userDataSource:UserDataSource
){

    val response = userDataSource.updateUserInfo(
        userId = userId,
        firstName = userUpdate.firstName,
        lastName = userUpdate.lastName
    )

    if (response){
        app.log.info("USER SUCCESSFULLY UPDATED")
        call.respond(
            message = ApiResponse(
                success = response,
                message = "Succesfull Updated"
            ),
            status = HttpStatusCode.OK
        )
    }else{
        app.log.info("ERROR UPDATING USER ")
        call.respond(
            message = ApiResponse(success = false),
            status = HttpStatusCode.BadRequest
        )
    }




}