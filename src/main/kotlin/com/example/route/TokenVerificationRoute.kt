package com.example.route

import com.example.domain.model.ApiRequest
import com.example.domain.model.EndPoints
import com.example.domain.model.User
import com.example.domain.model.UserSession
import com.example.domain.repository.UserDataSource
import com.example.util.Constants.AUDIENCE
import com.example.util.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.tokenVerificationRoute(app: Application,userDataSource: UserDataSource) {

    post(EndPoints.TokenVerification.path) {

        val request = call.receive<ApiRequest>()

        app.log.info(" ${request.tokenId}")


        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId,app)
            app.log.info("request is $result")



            if (result != null) {

                saveUserToDatabase(
                    app = app,
                    result = result,
                    userDataSource = userDataSource

                )

            } else {
                app.log.info("TOKEN VERIFICATION FAILED")
                call.respondRedirect(EndPoints.Unauthorized.path)
            }

        } else {
            app.log.info("EMPTY TOKEN ID ")
            call.respondRedirect(EndPoints.Unauthorized.path)
        }


    }

}

private suspend fun PipelineContext<Unit,ApplicationCall>.saveUserToDatabase(
    app: Application,
    result:GoogleIdToken,
    userDataSource: UserDataSource
) {
    app.log.info("TOKEN VERIFICATION SUCCESS")

    val sub = result.payload["sub"].toString()
    val name = result.payload["name"].toString()
    val email = result.payload["email"].toString()
    val profilePhoto = result.payload["picture"].toString()


    val user = User(
        id = sub,
        name = name,
        emailAddress = email,
        profilePhoto = profilePhoto
    )

    val response = userDataSource.saveUserInfo(user)

    if(response){
        app.log.info("TOKEN VERIFICATION SUCCESS ,USER SAVED/RETRIEVED" )
        call.sessions.set(UserSession(id = sub,name= name))
        call.respondRedirect(EndPoints.Authorized.path)
    }  else {
        app.log.info("ERROR SAVING USER")
        call.respondRedirect(EndPoints.Unauthorized.path)
    }





}


fun verifyGoogleTokenId(tokenId: String?,app: Application): GoogleIdToken? {

    try {

        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()
        return  verifier.verify(tokenId)

    }catch (ex: Exception) {

        app.log.info("Error message ${ex.message.toString()}")
        return null
    }


}