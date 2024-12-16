package com.coderse.routing

import com.coderse.model.User
import com.coderse.routing.request.UserRequest
import com.coderse.routing.response.UserResponse
import com.coderse.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.util.UUID
import java.util.*

fun Route.userRoute(
    userService: UserService
) {

    post {
        val userRequest = call.receive<UserRequest>()

        val createdUser = userService.save(
            user = userRequest.toModel()
        ) ?: return@post call.respond(HttpStatusCode.BadRequest)

        call.response.header(
            name = "id",
            value = createdUser.id.toString()
        )

        call.respond(HttpStatusCode.Created)
    }

    authenticate {
        get {
            val users = userService.findAll()

            call.respond(
                message = users.map(User::toResponse)
            )
        }
    }


    authenticate ("another-auth"){
        get("/{id}") {
            val id: String  = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val foundUser = userService.findById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            if(foundUser.username != extractPrincipalUsername(call))
                return@get call.respond(HttpStatusCode.NotFound)
            call.respond(
                message = foundUser.toResponse()
            )
        }
    }
}

fun extractPrincipalUsername(call: ApplicationCall): String? =
    call.principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("username")
        ?.asString()

private fun UserRequest.toModel(): User =
    User(
        id = UUID.randomUUID(),
        username = this.username,
        password = this.password,
    )

private fun User.toResponse(): UserResponse =
    UserResponse(
        id = this.id,
        username = this.username
    )