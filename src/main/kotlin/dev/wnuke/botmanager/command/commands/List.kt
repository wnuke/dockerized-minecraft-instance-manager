package dev.wnuke.botmanager.command.commands

import dev.wnuke.botmanager.command.Command
import dev.wnuke.botmanager.dockerAPI

class List : Command() {
    override val aliases: Array<String> = arrayOf("list", "instances", "ls", "l")

    override fun run() {
        val botInstances = dockerAPI.getBotInstances()
        println("There are ${botInstances.size} instance${if (botInstances.size != 1) "s" else ""}${if (botInstances.size != 0) ":" else "."}")
        for (instance in botInstances) {
            val port: String = if (instance.ports.isNotEmpty()) instance.ports[0].publicPort.toString() else "no ports"
            println("name: ${instance.names.getOrElse(0) { "unnamed" }}, id: ${instance.id.substring(0..11)}, number: $port, state: ${instance.state}")
        }
    }
}