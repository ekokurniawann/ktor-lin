package com.coderse.routing.response

import com.coderse.util.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val username: String,
)
