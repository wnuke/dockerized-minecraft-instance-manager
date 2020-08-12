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
class Login : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(StringArgument("username", "Username to login as.", true),
            StringArgument("password", "Password of the account to login as.", false))
    override val aliases: Array<String> = arrayOf("login")

    override fun run() {
        if (dockerAPI.instanceExists(currentInstance)) {
            GlobalScope.launch {
                try {
                    println("Attempting to login instance $currentInstance as ${arguments[0].value} with password ${arguments[1].value}...")
                    minecraftAPI.login(currentInstance, arguments[0].value as String, arguments[1].value as String)
                } catch (e: ServerResponseException) {
                    println("Login failed.")
                }
            }
        } else {
            println(noInstance(currentInstance.toString()))
        }
    }
}
