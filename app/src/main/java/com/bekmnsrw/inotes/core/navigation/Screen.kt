package com.bekmnsrw.inotes.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.Note
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {

    object NoteDetails : Screen(route = "{noteId}/details") {
        fun createRoute(noteId: Long) = "$noteId/details"
    }
}

sealed class BottomNavigationItem(
    val route: String,
    val icon: ImageVector
) {

    object NotesList : BottomNavigationItem(
        route = "notes-list",
        icon = Icons.Rounded.Note
    )

    object ToDoList : BottomNavigationItem(
        route = "todo-list",
        icon = Icons.Rounded.CheckBox
    )

    object Settings : BottomNavigationItem(
        route = "settings",
        icon = Icons.Rounded.Settings
    )
}
