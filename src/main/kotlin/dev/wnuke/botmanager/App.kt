package dev.wnuke.botmanager

import dev.wnuke.botmanager.command.CommandManager
import dev.wnuke.botmanager.docker.DockerAPI
import dev.wnuke.botmanager.minecraft.MinecraftAPI

lateinit var dockerAPI: DockerAPI
lateinit var minecraftAPI: MinecraftAPI

fun main(args: Array<String>) {
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

