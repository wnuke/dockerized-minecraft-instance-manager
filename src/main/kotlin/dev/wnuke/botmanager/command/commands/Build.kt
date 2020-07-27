package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.StringArgument
import dev.wnuke.botmanager.dockerAPI

class Build : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(StringArgument("build path", "Path to the folder containg the Dockerfile.", false))
    override val aliases: Array<String> = arrayOf("build", "b")

    override fun run() {
        if (dockerAPI.buildBotImage(arguments[0].value as String)) {
            println("Successfully built the bot image.")
        } else {
            println("Image build failed, please check that you specified the correct build folder.")
        }
    }
}