package com.coderse.routing

import com.coderse.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService
){
    routing{

        route("/api/user"){
            userRoute(userService)
        }
    }
}