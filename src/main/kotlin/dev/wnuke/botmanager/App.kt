package dev.wnuke.botmanager

import dev.wnuke.botmanager.command.CommandManager
import dev.wnuke.botmanager.docker.DockerAPI
import dev.wnuke.botmanager.minecraft.MinecraftAPI

/**
 * Instance of the Docker API Client
 */
lateinit var dockerAPI: DockerAPI
/**
 * Instance of the Minecraft Bot API Client
 */
lateinit var minecraftAPI: MinecraftAPI

/**
 * Main method to start the app
 */
fun main() {
    dockerAPI = DockerAPI()
    minecraftAPI = MinecraftAPI()
    val commandManager = CommandManager()
    while (true) {
        print("instance-manager > ")
        val input = readLine()
        if (input != null) {
            commandManager.execute(input)
        }
    }
}

