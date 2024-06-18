package com.abloom.mery.data.firebase

import dev.gitlive.firebase.firestore.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun Timestamp.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

fun LocalDateTime.toTimestamp(): Timestamp {
    val zoneId = ZoneId.systemDefault()
    val epochSecond = atZone(zoneId).toEpochSecond()
    return Timestamp(epochSecond, nano)
}

fun Timestamp.toLocalDate(): LocalDate =
    Instant.ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

fun LocalDate.toTimestamp(): Timestamp {
    val zoneId = ZoneId.systemDefault()
    val epochSecond = atStartOfDay(zoneId).toEpochSecond()
    return Timestamp(epochSecond, 0)
}
