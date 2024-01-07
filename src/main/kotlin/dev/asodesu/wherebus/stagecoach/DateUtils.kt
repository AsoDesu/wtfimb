package dev.asodesu.wherebus.stagecoach

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

val UTC_ZONE = ZoneId.of("UTC")

fun getTime(date: ZonedDateTime): String {
    var amPm = "PM"
    var hour = date.hour
    if (hour > 12) {
        hour -= 12
    } else if (hour < 12) {
        amPm = "AM"
    }
    return String.format("%d:%02d %s", hour, date.minute, amPm)
}

fun parseSeconds(iso: String): Long {
    return Instant.parse(iso).epochSecond
}