package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.command.arguments.Argument
import dev.wnuke.botmanager.dockerAPI

class List : Command() {
    override val arguments: Array<Argument<out Any>> = emptyArray()
    override val aliases: Array<String> = arrayOf("list", "instances")

    override fun run() {
        val botInstances = dockerAPI.getBotInstances()
        println("There are ${botInstances.size} instance${if (botInstances.size != 1) "s" else ""}${if (botInstances.size != 0) ":" else "."}")
        for (instance in botInstances) {
            println("${instance.names.getOrElse(0) { "unnamed" }}: ${instance.id}, ${instance.ports.getOrElse(0) { "no ports" }}")
        }
    }
}