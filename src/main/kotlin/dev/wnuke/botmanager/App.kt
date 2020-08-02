package dev.wnuke.botmanager

import dev.wnuke.botmanager.command.CommandManager
import dev.wnuke.botmanager.docker.DockerAPI
import dev.wnuke.botmanager.minecraft.MinecraftAPI
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Instance of the Docker API Client
 */
lateinit var dockerAPI: DockerAPI

/**
 * Instance of the Minecraft Bot API Client
 */
lateinit var minecraftAPI: MinecraftAPI

/**
 * Main method to start the app
 */
fun main() {
    dockerAPI = DockerAPI()
    minecraftAPI = MinecraftAPI()
    val commandManager = CommandManager()
    while (true) {
        print("instance-manager > ")
        val input = readLine()
        if (input != null) {
            commandManager.execute(input)
        }
    }
}

/**
 * Splits a String by quotes or spaces
 * @param string  String to split
 * @return Result of the split
 */
fun splitStringByQuotes(string: String): Array<String> {
    val splitString: MutableList<String> = ArrayList()
    val regex: Pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'")
    val regexMatcher: Matcher = regex.matcher(string)
    while (regexMatcher.find()) {
        when {
            regexMatcher.group(1) != null -> splitString.add(regexMatcher.group(1))
            regexMatcher.group(2) != null -> splitString.add(regexMatcher.group(2))
            else -> splitString.add(regexMatcher.group())
        }
    }
    return splitString.toTypedArray()
}

