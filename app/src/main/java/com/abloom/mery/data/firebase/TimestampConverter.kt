package com.abloom.mery.data.firebase

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

fun Timestamp.toLocalDate(): LocalDate = toDate().toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun LocalDate.toTimestamp(): Timestamp {
    val zoneId = ZoneId.systemDefault()
    val instant = atStartOfDay(zoneId).toInstant()
    val date = Date.from(instant)
    return Timestamp(date)
}

fun Timestamp.toLocalDateTime(): LocalDateTime = toDate().toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

fun LocalDateTime.toTimestamp(): Timestamp {
    val zoneId = ZoneId.systemDefault()
    val instant = atZone(zoneId).toInstant()
    val date = Date.from(instant)
    return Timestamp(date)
}

fun dev.gitlive.firebase.firestore.Timestamp.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

fun LocalDateTime.toTimestamp1(): dev.gitlive.firebase.firestore.Timestamp {
    val zoneId = ZoneId.systemDefault()
    val epochSecond = atZone(zoneId).toEpochSecond()
    return dev.gitlive.firebase.firestore.Timestamp(epochSecond, nano)
}

fun dev.gitlive.firebase.firestore.Timestamp.toLocalDate(): LocalDate =
    Instant.ofEpochSecond(seconds, nanoseconds.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

fun LocalDate.toTimestamp1(): dev.gitlive.firebase.firestore.Timestamp {
    val zoneId = ZoneId.systemDefault()
    val epochSecond = atStartOfDay(zoneId).toEpochSecond()
    return dev.gitlive.firebase.firestore.Timestamp(epochSecond, 0)
}
