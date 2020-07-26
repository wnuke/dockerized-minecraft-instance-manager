package dev.wnuke.botmanager

import dev.wnuke.botmanager.command.CommandManager

lateinit var dockerAPI: DockerAPI

fun main(args: Array<String>) {
    dockerAPI = DockerAPI()
    val commandManager = CommandManager()
    while (true) {
        print("instance-manager > ")
        val input = readLine()
        if (input != null) {
            commandManager.execute(input)
        }
    }
}

