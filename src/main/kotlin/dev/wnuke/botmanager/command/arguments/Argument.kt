package dev.wnuke.botmanager.command.arguments

/**
 * Abstract argument for creating more arguments
 * @property T  Primitive type of the argument
 */
abstract class Argument<T>(
        /**
         * Name of the argument (for use in a help command)
         */
        val name: String,
        /**
         * Description of the argument (for use in a help command)
         */
        val description: String,
        /**
         * Whether or not the argument is required, if it is required but is missing command execution will fail
         */
        val required: Boolean) {
    /**
     * Content of the argument
     */
    abstract var value: T

    /**
     * Sets the value of the argument based on a String
     * Should throw a NullPointerException when parsing fails
     */
    @Throws(NullPointerException::class)
    abstract fun parse(input: String)
}
