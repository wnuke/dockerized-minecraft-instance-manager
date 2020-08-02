package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.dockerAPI

/**
 * Command to destroy all instances
 */
class DestroyAll : Command() {
    override val aliases: Array<String> = arrayOf("destroyall")

    override fun run() {
        dockerAPI.destroyAllBotInstances()
        println("Destroyed all instances.")
    }
}