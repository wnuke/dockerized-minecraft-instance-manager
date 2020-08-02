package dev.wnuke.botmanager.command

import dev.wnuke.botmanager.command.commands.*
import dev.wnuke.botmanager.command.commands.List
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


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
        val argumentString = if (commandSplit.size > 1) commandSplit[1] else ""
        val arguments: MutableList<String> = ArrayList()
        val regex: Pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'")
        val regexMatcher: Matcher = regex.matcher(argumentString)
        while (regexMatcher.find()) {
            when {
                regexMatcher.group(1) != null -> arguments.add(regexMatcher.group(1))
                regexMatcher.group(2) != null -> arguments.add(regexMatcher.group(2))
                else -> arguments.add(regexMatcher.group())
            }
        }
        for (command in commands) {
            if (commandAlias in command.aliases) {
                command.exec(arguments.toTypedArray())
                return
            }
        }
        println("Command \"$commandAlias\" does not exist.")
    }
}