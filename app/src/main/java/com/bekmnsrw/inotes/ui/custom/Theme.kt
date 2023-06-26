package com.bekmnsrw.inotes.ui.custom

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = colorsPalette(darkTheme = darkTheme)
    val typography = typography()
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content
    )
}

@Composable
fun colorsPalette(darkTheme: Boolean): Colors = when {
    darkTheme -> baseDarkPalette
    else -> baseLightPalette
}

@Composable
fun typography(): Typography = Typography(
    screenHeading = TextStyle(
        fontSize = 54.sp,
        fontWeight = FontWeight.Medium
    ),
    cardTitle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium
    ),
    cardContent = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal
    ),
    cardDate = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    noteTitle = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium
    ),
    noteContent = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    tag = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal
    ),
    notesCount = TextStyle(
        fontSize = 48.sp,
        fontWeight = FontWeight.Medium
    )
)
