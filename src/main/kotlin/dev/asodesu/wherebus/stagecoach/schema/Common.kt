package dev.asodesu.wherebus.stagecoach.schema

class Value<T>(
    val value: T,
)

enum class Direction {
    INBOUND,
    OUTBOUND;

    companion object {
        fun fromChar(char: Char): Direction? {
            return entries.firstOrNull {
                it.name.startsWith(char.uppercase())
            }
        }
    }
}