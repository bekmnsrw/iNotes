package com.bekmnsrw.inotes.core.navigation

sealed class NavigationGraph(
    val startDestination: String,
    val route: String
) {

    object NotesGraph : NavigationGraph(
        startDestination = BottomNavigationItem.NotesList.route,
        route = "notes_graph"
    )

    object ToDoGraph : NavigationGraph(
        startDestination = BottomNavigationItem.ToDoList.route,
        route = "todo_graph"
    )

    object SettingsGraph : NavigationGraph(
        startDestination = BottomNavigationItem.Settings.route,
        route = "settings_graph"
    )
}
