package dev.wnuke.botmanager.minecraft

import kotlinx.serialization.Serializable

/**
 * Template for sending a server connect request to the Minecraft API
 */
@Serializable
data class ServerConnect(
        /**
         * Address of the server to connect to
         */
        val address: String,
        /**
         * Port to connect to
         */
        val port: String)

/**
 * Template for sending a message send request to the Minecraft API
 */
@Serializable
data class Message(
        /**
         * Message to send
         */
        val message: String)

/**
 * Template for sending a login request to the Minecraft API
 */
@Serializable
data class Login(
        /**
         * Username to login as
         */
        val username: String,
        /**
         * Password to use for authentication
         */
        val password: String)