package com.example.route

import com.example.domain.model.ApiRequest
import com.example.domain.model.EndPoints
import com.example.domain.model.UserSession
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

fun Route.tokenVerificationRoute(app: Application) {

    post(EndPoints.TokenVerification.path) {

        val request = call.receive<ApiRequest>()

        app.log.info("request is ${request.tokenId}")



        if (request.tokenId.isNotEmpty()) {

            val result = verifyGoogleTokeId(tokenId = request.tokenId,app)

            app.log.info("result is $result")



            if (result != null) {
                app.log.info("TOKEN VERIFICATION SUCCESS")

                // val sub = result.payload["sub"].toString()
                val name = result.payload["name"].toString()
                val email = result.payload["email"].toString()
                //  val profilePhoto = result.payload["picture"].toString()

                app.log.info("TOKEN VERIFICATION SUCCESS FOR $name with email of $email")



                call.sessions.set(UserSession(id = "123", "James"))
                call.respondRedirect(EndPoints.Authorized.path)
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

fun verifyGoogleTokeId(tokenId: String?,app: Application): GoogleIdToken? {

    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()
        verifier.verify(tokenId)
    } catch (ex: Exception) {
        app.log.info("Error message ${ex.message.toString()}")
        null
    }


}