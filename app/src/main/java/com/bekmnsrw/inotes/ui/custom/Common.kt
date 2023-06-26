package com.bekmnsrw.inotes.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class Colors(
    val background: Color,
    val onBackground: Color,
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val outline: Color
)

data class Typography(
    val screenHeading: TextStyle,
    val cardTitle: TextStyle,
    val cardContent: TextStyle,
    val cardDate: TextStyle,
    val noteTitle: TextStyle,
    val noteContent: TextStyle,
    val tag: TextStyle,
    val notesCount: TextStyle
)

object CustomTheme {
    val colors: Colors
        @Composable get() = LocalColors.current

    val typography: Typography
        @Composable get() = LocalTypography.current
}

val LocalColors = staticCompositionLocalOf<Colors> { error("No colors provided") }
val LocalTypography = staticCompositionLocalOf<Typography> { error("No typography provided") }
