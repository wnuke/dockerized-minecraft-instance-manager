package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.IntegerArgument
import dev.wnuke.botmanager.command.arguments.StringArgument
import dev.wnuke.botmanager.dockerAPI
import dev.wnuke.botmanager.minecraftAPI
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Connect : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(IntegerArgument("instance", "Port of the instance to connect.", true),
            StringArgument("address", "Address of the server to connect the instance to.", true),
            IntegerArgument("port", "Port of the server to connect to.", false))
    override val aliases: Array<String> = arrayOf("c", "connect")

    override fun run() {
        if (arguments[2].value == 0) {
            arguments[2].parse("25565")
        }
        val port = arguments[0].value as Int
        if (dockerAPI.instanceExists(port)) {
            GlobalScope.launch {
                minecraftAPI.connectToServer(port, arguments[1].value as String, arguments[2].value.toString())
            }
            println("Attempting to connect instance with port $port to ${arguments[1].value}:${arguments[2].value}...")
        } else {
            println("There is no instance with port $port.")
        }
    }
}