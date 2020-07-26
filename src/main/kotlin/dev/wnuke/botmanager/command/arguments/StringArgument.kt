package dev.wnuke.botmanager.command.arguments

class StringArgument(name: String, description: String, required: Boolean) : Argument<String>(name, description, required) {
    override var value: String = ""

    override fun parse(input: String) {
        value = input
    }
}