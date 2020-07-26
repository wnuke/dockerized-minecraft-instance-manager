package dev.wnuke.botmanager.command.arguments

abstract class Argument<T>(val name: String, val description: String, val required: Boolean) {
    abstract var value: T

    @Throws(NullPointerException::class)
    open fun parse(input: String) {

    }
}
