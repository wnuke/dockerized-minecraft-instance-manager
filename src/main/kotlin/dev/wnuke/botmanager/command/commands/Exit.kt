package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.dockerAPI
import kotlin.system.exitProcess

class Exit : Command() {
    override val aliases: Array<String> = arrayOf("exit", "q", "e")

    override fun run() {
        if (dockerAPI.destroyAllBotInstances()) {
            println("Destroyed all instances.")
        } else {
            println("Failed to destroy all instances, exiting anyways.")
        }
        exitProcess(0)
    }
}