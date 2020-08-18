package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.IntegerArgument
import dev.wnuke.botmanager.currentInstance
import dev.wnuke.botmanager.dockerAPI
import dev.wnuke.botmanager.noInstance

/**
 * Command to select an instance to perform actions on
 */
class Select : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(IntegerArgument("instance", "Port of the instance to connect.", true))
    override val aliases: Array<String> = arrayOf("s", "select")

    override fun run() {
        val port = arguments[0].value as Int
        when {
            dockerAPI.instanceExists(port) -> {
                currentInstance = port
                println("Instance $port is now selected")
            }
            dockerAPI.instanceExists(port + 10000) -> {
                currentInstance = port + 10000
                println("Instance $currentInstance is now selected")
            }
            else -> {
                println(noInstance(port.toString()))
            }
        }
    }
}
