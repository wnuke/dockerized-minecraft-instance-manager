package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.dockerAPI

/**
 * Command to destroy all stopped instances
 */
class Prune : Command() {
    override val aliases: Array<String> = arrayOf("prune")

    override fun run() {
        dockerAPI.pruneBotInstances()
        println("Destroyed all stopped instances.")
    }
}