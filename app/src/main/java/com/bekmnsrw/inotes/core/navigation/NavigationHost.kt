package com.bekmnsrw.inotes.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.bekmnsrw.inotes.core.navigation.BottomNavigationItem.*
import com.bekmnsrw.inotes.core.navigation.NavigationGraph.*
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsScreen
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListScreen
import com.bekmnsrw.inotes.feature.notes.presentation.tag.TagsScreen
import com.bekmnsrw.inotes.feature.settings.presentation.SettingsScreen
import com.bekmnsrw.inotes.feature.todo.presentation.ToDoListScreen
import com.bekmnsrw.inotes.ui.custom.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = NotesGraph.route
) {
    val items = listOf(
        NotesList,
        ToDoList,
        Settings
    )

    Scaffold(
        bottomBar = {
            CustomBottomAppBar(
                navHostController = navHostController,
                bottomNavigationItems = items
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation(
                startDestination = NotesGraph.startDestination,
                route = NotesGraph.route
            ) {
                composable(route = NotesList.route) {
                    NotesListScreen(
                        navController = navHostController
                    )
                }
                composable(route = NestedScreen.NoteDetails.route) { NoteDetailsScreen(navController = navHostController) }
                composable(route = NestedScreen.Tags.route) { TagsScreen(navController = navHostController) }
            }

            navigation(
                startDestination = ToDoGraph.startDestination,
                route = ToDoGraph.route
            ) {
                composable(route = ToDoList.route) { ToDoListScreen() }
            }

            navigation(
                startDestination = SettingsGraph.startDestination,
                route = SettingsGraph.route
            ) {
                composable(route = Settings.route) { SettingsScreen() }
            }
        }
    }
}

@Composable
private fun CustomBottomAppBar(
    navHostController: NavHostController,
    bottomNavigationItems: List<BottomNavigationItem>
) {
    BottomNavigation(
        backgroundColor = CustomTheme.colors.primary,
        contentColor = CustomTheme.colors.onPrimary
    ) {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val graphs = listOf(NotesGraph.route, ToDoGraph.route, SettingsGraph.route)
        val currentGraph = currentDestination?.hierarchy?.firstOrNull { it.parent?.route in graphs }?.parent?.route

        bottomNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                selected = when {
                    item.route == NotesList.route && currentGraph == NotesGraph.route -> true
                    item.route == ToDoList.route && currentGraph == ToDoGraph.route -> true
                    item.route == Settings.route && currentGraph == SettingsGraph.route -> true
                    else -> false
                },
                onClick = {
                    navHostController.navigate(item.route) {
                        popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
