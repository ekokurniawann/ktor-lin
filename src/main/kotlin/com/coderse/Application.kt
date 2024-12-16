package com.coderse

import com.coderse.plugins.configureSerialization
import com.coderse.repository.UserRepository
import com.coderse.routing.configureRouting
import com.coderse.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)


    configureSerialization()
    configureRouting(userService)
//    configureSecurity()
//    configureSerialization()
//    configureRouting()
}