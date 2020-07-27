package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.command.arguments.IntegerArgument
import dev.wnuke.botmanager.dockerAPI

class Destroy : Command() {
    override val arguments: Array<Argument<out Any>> = arrayOf(IntegerArgument("port", "Port of the instance you want to destroy.", true))
    override val aliases: Array<String> = arrayOf("destroy", "rm", "del", "delete", "d")

    override fun run() {
        if (dockerAPI.destroyBotInstance(arguments[0].value as Int)) {
            println("Instance destroyed successfully.")
        } else {
            println("Failed to destroy instance, there is probably no instance with that port.")
        }
    }
}