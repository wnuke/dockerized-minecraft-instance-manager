package dev.wnuke.botmanager.minecraft

/**
 * Template for chat get request responses from the Minecraft API
 */
data class Chat(
        /**
         * ArrayList of the recieved messages
         */
        val messages: ArrayList<String>)

/**
 * Template for player information request responses from the Minecraft API
 */
data class Player(
        /**
         * Name of the logged in account
         */
        val name: String,
        /**
         * UUID of the logged in account
         */
        val uuid: String,
        /**
         * More details on the
         */
        val player: PlayerInfo,
        /**
         * Players current location in the world
         */
        val coordinates: Position)

/**
 * Data class to complement the Player class
 * @see Player
 */
data class PlayerInfo(
        /**
         * Health level
         */
        val health: Float,
        /**
         * Hunger level
         */
        val hunger: Float,
        /**
         * Saturation level
         */
        val saturation: String)

/**
 * Data class to complement the Player class, contains the in world position of the player
 * @see Player
 */
data class Position(
        /**
         * X coordinate
         */
        val x: Double,
        /**
         * Y coordinate
         */
        val y: Double,
        /**
         * Z coordinate
         */
        val z: Double)