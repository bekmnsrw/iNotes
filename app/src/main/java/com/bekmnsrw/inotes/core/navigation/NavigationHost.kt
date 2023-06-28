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
import com.bekmnsrw.inotes.feature.notes.presentation.details.NoteDetailsScreen
import com.bekmnsrw.inotes.feature.notes.presentation.list.NotesListScreen
import com.bekmnsrw.inotes.feature.settings.presentation.SettingsScreen
import com.bekmnsrw.inotes.feature.todo.presentation.ToDoListScreen
import com.bekmnsrw.inotes.ui.custom.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = BottomNavigationItem.NotesList.route
) {
    val items = listOf(
        BottomNavigationItem.NotesList,
        BottomNavigationItem.ToDoList,
        BottomNavigationItem.Settings
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
            composable(BottomNavigationItem.NotesList.route) { NotesListScreen(navController = navHostController) }
            composable(BottomNavigationItem.ToDoList.route) { ToDoListScreen() }
            composable(BottomNavigationItem.Settings.route) { SettingsScreen() }

            composable(Screen.NoteDetails.route) { NoteDetailsScreen(navController = navHostController) }
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

        bottomNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
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
