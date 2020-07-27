package dev.wnuke.botmanager.minecraft

data class Chat(val messages: ArrayList<String>)

data class Player(val name: String, val uuid: String, val player: PlayerInfo, val coordinates: Position)

data class PlayerInfo(val health: Float, val hunger: Float, val saturation: String)

data class Position(val x: Double, val y: Double, val z: Double)