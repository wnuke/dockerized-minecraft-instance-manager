package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.StringArgument
import dev.wnuke.botmanager.currentInstance
import dev.wnuke.botmanager.dockerAPI
import dev.wnuke.botmanager.minecraftAPI
import dev.wnuke.botmanager.noInstance
import io.ktor.client.features.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Command to tell an instance to login with a username and password
 */
class Message : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(StringArgument("message", "Message to send.", true))
    override val aliases: Array<String> = arrayOf("message", "msg", "m")

    override fun run() {
        if (dockerAPI.instanceExists(currentInstance)) {
            GlobalScope.launch {
                try {
                    println("Sending message from $currentInstance...")
                    minecraftAPI.sendMessage(currentInstance, arguments[0].value as String)
                } catch (e: ServerResponseException) {
                    println("Failed to send message.")
                }
            }
        } else {
            println(noInstance(currentInstance.toString()))
        }
    }
}
