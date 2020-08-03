package dev.wnuke.botmanager.command.arguments

/**
 * Argument requiring a String
 */
class StringArgument(name: String, description: String, required: Boolean) : Argument<String>(name, description, required) {
    override var value: String = ""

    override fun parse(input: String, index: Int) {
        value = input
    }
}