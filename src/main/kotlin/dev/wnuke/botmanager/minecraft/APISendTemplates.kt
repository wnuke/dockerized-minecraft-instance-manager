package dev.wnuke.botmanager.minecraft

data class ServerConnect(val address: String, val port: String)

data class Message(val message: String)

data class Login(val username: String, val password: String)