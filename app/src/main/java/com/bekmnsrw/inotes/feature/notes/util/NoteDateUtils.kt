package com.bekmnsrw.inotes.feature.notes.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar

fun getCurrentDateTimeInMilliseconds(): Long = System.currentTimeMillis()

@SuppressLint("SimpleDateFormat")
fun formatLastModifiedInNotesList(lastModified: Long): String {
    val dateTime = SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm").format(lastModified)
    val fullDate = dateTime.split(", ")[1]
    val time = dateTime.split(", ")[2]
    val date = "${fullDate.split(" ")[0]} ${fullDate.split(" ")[1]}"
    val year = fullDate.split(" ")[2]

    return when {
        DateUtils.isToday(lastModified + DateUtils.DAY_IN_MILLIS) -> "Yesterday, $time"
        DateUtils.isToday(lastModified) -> "Today, $time"
        Calendar.getInstance().get(Calendar.YEAR).toString() == year -> date
        else -> fullDate
    }
}

@SuppressLint("SimpleDateFormat")
fun formatLastModifiedInNoteDetails(
    lastModified: Long
): String = SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm").format(lastModified)
