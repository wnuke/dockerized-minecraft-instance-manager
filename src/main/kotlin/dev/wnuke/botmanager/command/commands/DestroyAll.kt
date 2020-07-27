package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.dockerAPI

class DestroyAll : Command() {
    override val aliases: Array<String> = arrayOf("destroyall")

    override fun run() {
        if (dockerAPI.destroyAllBotInstances()) {
            println("Destroyed all instances.")
        } else {
            println("An error occurred while destroying all instances.")
        }
    }
}