package dev.wnuke.botmanager.command

import dev.wnuke.botmanager.command.arguments.Argument

abstract class Command {
    open val arguments: Array<Argument<out Any>> = emptyArray()
    abstract val aliases: Array<String>

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
                        arg.parse(args[i])
                    } catch (e: NullPointerException) {
                        println(e.localizedMessage)
                        return
                    }
                }
            }
            run()
        }
    }

    abstract fun run()
}