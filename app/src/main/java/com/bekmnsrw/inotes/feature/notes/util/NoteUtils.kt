package com.bekmnsrw.inotes.feature.notes.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.text.SimpleDateFormat
import java.util.Calendar

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> state = event }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    return state
}

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
