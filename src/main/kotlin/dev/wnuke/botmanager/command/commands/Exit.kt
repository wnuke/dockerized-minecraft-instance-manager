package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import kotlin.system.exitProcess

/**
 * Command to exit the cli
 */
class Exit : Command() {
    override val aliases: Array<String> = arrayOf("exit", "q", "e")

    override fun run() {
        exitProcess(0)
    }
}