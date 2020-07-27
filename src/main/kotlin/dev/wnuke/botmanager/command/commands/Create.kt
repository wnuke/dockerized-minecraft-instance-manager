package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.StringArgument
import dev.wnuke.botmanager.dockerAPI

class Create: Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(StringArgument("username", "The username for the account.", false), StringArgument("password", "The password for the account.", false))
    override val aliases: Array<String> = arrayOf("create", "add", "new", "a", "n")

    override fun run() {
        if (dockerAPI.createBotInstance(arguments[0].value as String, arguments[1].value as String)) {
            println("Instance created successfully.")
        } else {
            println("Container creation failed, have you built the image yet?")
        }
    }
}