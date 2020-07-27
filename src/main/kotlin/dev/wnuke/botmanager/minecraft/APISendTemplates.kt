package dev.wnuke.botmanager.minecraft

import kotlinx.serialization.Serializable

@Serializable
data class ServerConnect(val address: String, val port: String)

@Serializable
data class Message(val message: String)

@Serializable
data class Login(val username: String, val password: String)