package dev.wnuke.botmanager.minecraft

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.contentType

/**
 * Client to interact with the API of Headless Minecraft bot instances
 */
class MinecraftAPI {
    private val apiAddressPrefix = "http://localhost:"
    private val json = io.ktor.client.features.json.defaultSerializer()
    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    /**
     * Gets information about the player of a bot
     * @param instancePort  Port of the instance to get player information of
     */
    suspend fun getPlayer(instancePort: Int): Player {
        return client.get("$apiAddressPrefix$instancePort/player")
    }

    /**
     * Gets the recieved chat messages of a bot
     * @param instancePort  Port of the instance to get chat history of
     */
    suspend fun getChat(instancePort: Int): Chat {
        return client.get("$apiAddressPrefix$instancePort/chat")
    }

    /**
     * Tells an instance of the bot to connect to a server
     * @param instancePort  Port of the instance to connect to a server
     * @param serverAddress  Address of the server to connect to
     * @param serverPort  Port of the server to connect to
     */
    suspend fun connectToServer(instancePort: Int, serverAddress: String, serverPort: String = "25565") {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/connect")
            contentType(io.ktor.http.ContentType.Application.Json)
            body = ServerConnect(serverAddress, serverPort)
        }
    }

    /**
     * Tells an instance of the bot to login to a Minecraft account
     * @param instancePort  Port of the instance to login
     * @param username  Username of the Minecraft account
     * @param password  Password of the Minecraft account, if blank or incorrect will login as an offline user
     */
    suspend fun login(instancePort: Int, username: String, password: String) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/login")
            contentType(io.ktor.http.ContentType.Application.Json)
            body = json.write(Login(username, password))
        }
    }

    /**
     * Tells an instance of the bot to disconnect from the server it is currently connected to, if it is not connected to a server nothing happens
     * @param instancePort  Port of the instance to disconnect
     */
    suspend fun disconnectFromServer(instancePort: Int) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/disconnect")
        }
    }

    /**
     * Tells an instance of the bot to send a chat message, exactly like typing the message directly into chat
     * (if the message is a minecraft command (preceded by a "/") or is a command of any other mods it will be interpreted as such)
     * @param instancePort  Port of the instance the message should be sent from
     * @param message  Message to send
     */
    suspend fun sendMessage(instancePort: Int, message: String) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/sendmsg")
            contentType(io.ktor.http.ContentType.Application.Json)
            body = json.write(Message(message))
        }
    }
}