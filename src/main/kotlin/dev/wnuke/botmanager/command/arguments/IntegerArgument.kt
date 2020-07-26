package dev.wnuke.botmanager.command.arguments

class IntegerArgument(name: String, description: String, required: Boolean) : Argument<Int>(name, description, required) {
    override var value: Int = 0

    override fun parse(input: String) {
        if (input.toIntOrNull() != null) {
            value = input.toInt()
        } else {
            throw NullPointerException("Invalid input!")
        }
    }
}