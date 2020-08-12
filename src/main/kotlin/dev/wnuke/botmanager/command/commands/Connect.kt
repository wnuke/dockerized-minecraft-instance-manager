package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.IntegerArgument
import dev.wnuke.botmanager.command.arguments.StringArgument
import dev.wnuke.botmanager.currentInstance
import dev.wnuke.botmanager.dockerAPI
import dev.wnuke.botmanager.minecraftAPI
import dev.wnuke.botmanager.noInstance
import io.ktor.client.features.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Command to tell an instance to connect to a server
 */
class Connect : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(StringArgument("address", "Address of the server to connect the instance to.", true),
            IntegerArgument("port", "Port of the server to connect to.", false))
    override val aliases: Array<String> = arrayOf("c", "connect")

    override fun run() {
        var port = arguments[1].value as Int
        if (port == 0) {
            port = 25565
        }
        if (dockerAPI.instanceExists(currentInstance)) {
            GlobalScope.launch {
                try {
                    println("Attempting to connect instance $currentInstance to ${arguments[0].value}:$port...")
                    minecraftAPI.connectToServer(currentInstance, arguments[0].value as String, port.toString())
                } catch (e: ServerResponseException) {
                    println("Connection failed.")
                }
            }
        } else {
            println(noInstance(currentInstance.toString()))
        }
    }
}
