package com.bekmnsrw.inotes.core.navigation

sealed class NestedScreen(val route: String) {

    object NoteDetails : NestedScreen(route = "details/{noteId}") {
        fun createRoute(noteId: Long) = "details/$noteId"
    }

    object Tags : NestedScreen(route = "tags/{selectedTagId}") {
        fun createRoute(selectedTagId: Long) = "tags/$selectedTagId"
    }
}
