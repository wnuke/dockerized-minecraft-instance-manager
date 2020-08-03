package dev.wnuke.botmanager.command

import dev.wnuke.botmanager.command.arguments.Argument
import java.text.ParseException

/**
 * Abstract class for creating commands for the cli.
 */
abstract class Command {
    /**
     * Array of that are available for the command.
     */
    open val arguments: Array<Argument<out Any>> = emptyArray()

    /**
     * Names that will be cause this command to be called.
     */
    abstract val aliases: Array<String>

    /**
     * Parse the arguments of the command and then run the command.
     *
     * @param args  Array of strings to parse as arguments for the command
     */
    fun exec(args: Array<String>) {
        if (arguments.isEmpty()) {
            run()
        } else {
            for ((i, arg) in arguments.withIndex()) {
                if (args.size <= i && arg.required) {
                    println("Missing non-optional argument ${arg.name}: ${arg.description}")
                    return
                } else if (args.size <= i) {
                    break
                } else {
                    try {
                        arg.parse(args[i], i)
                    } catch (e: ParseException) {
                        println(e.localizedMessage)
                        return
                    }
                }
            }
            run()
        }
    }

    /**
     * Defines what the command does.
     */
    abstract fun run()
}