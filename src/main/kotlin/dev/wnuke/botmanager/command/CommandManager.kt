package dev.wnuke.botmanager.command

import dev.wnuke.botmanager.command.commands.*
import dev.wnuke.botmanager.command.commands.List
import dev.wnuke.botmanager.splitStringByQuotes


/**
 * Manages commands and handles their execution
 */
class CommandManager {
    private val commands = hashSetOf(Build(), Connect(), Create(), Destroy(), DestroyAll(), Exit(), List(), Prune())

    /**
     * Finds and executes a command by name and splits the arguments string into substrings to be used as arguments
     * @param commandWithArgs  String to split into a command and its arguments
     */
    fun execute(commandWithArgs: String) {
        if (commandWithArgs.isEmpty() || commandWithArgs.isBlank()) return
        val commandSplit = commandWithArgs.split(' ', ignoreCase = true, limit = 2)
        val commandAlias = if (commandSplit.isNotEmpty()) commandSplit[0] else commandWithArgs
        for (command in commands) {
            if (commandAlias in command.aliases) {
                command.exec(splitStringByQuotes(commandSplit[1]))
                return
            }
        }
        println("Command \"$commandAlias\" does not exist.")
    }
}