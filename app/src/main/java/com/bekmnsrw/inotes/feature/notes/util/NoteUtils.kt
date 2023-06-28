package com.bekmnsrw.inotes.feature.notes.util

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

fun getCurrentTime(): String = SimpleDateFormat("EEEE, d MMMM yyyy, HH:mm").format(Calendar.getInstance().time)

fun formatLastModified(lastModified: String): String {
    val fullDate = lastModified.split(", ")
    val date = fullDate[1].split(" ")
    val calendar = Calendar.getInstance()

    return when {
        DateUtils.isToday(calendar.time.time + DateUtils.DAY_IN_MILLIS) -> "Yesterday ${fullDate[2]}"
        DateUtils.isToday(calendar.time.time) -> fullDate[2]
        calendar.get(Calendar.YEAR).toString() == date[2] -> "${date[0]} ${date[1]}"
        else -> "${date[0]} ${date[1]} ${date[2]}"
    }
}
