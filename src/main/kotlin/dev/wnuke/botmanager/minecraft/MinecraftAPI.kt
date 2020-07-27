package dev.wnuke.botmanager.minecraft

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url

class MinecraftAPI {
    private val apiAddressPrefix = "http://localhost:"
    private val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer {
                serializeNulls()
                disableHtmlEscaping()
            }
        }
    }

    suspend fun getPlayer(instancePort: Int): Player {
        return client.get("$apiAddressPrefix$instancePort/player")
    }

    suspend fun getChat(instancePort: Int): Chat {
        return client.get("$apiAddressPrefix$instancePort/chat")
    }

    suspend fun connectToServer(instancePort: Int, serverAddress: String, serverPort: String) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/connect")
            body = ServerConnect(serverAddress, serverPort)
        }
    }

    suspend fun login(instancePort: Int, username: String, password: String) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/login")
            body = Login(username, password)
        }
    }

    suspend fun disconnectFromServer(instancePort: Int) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/disconnect")
        }
    }

    suspend fun sendMessage(instancePort: Int, message: String) {
        client.post<Unit> {
            url("$apiAddressPrefix$instancePort/sendmsg")
            body = Message(message)
        }
    }
}