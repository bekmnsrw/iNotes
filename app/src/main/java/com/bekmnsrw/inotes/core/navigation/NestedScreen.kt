package com.bekmnsrw.inotes.core.navigation

sealed class NestedScreen(val route: String) {

    object NoteDetails : NestedScreen(route = "details/{noteId}/{tagId}") {
        fun createRoute(
            noteId: Long,
            tagId: Long
        ) = "details/$noteId/$tagId"
    }

    object Tags : NestedScreen(route = "tags/{selectedTagId}") {
        fun createRoute(selectedTagId: Long) = "tags/$selectedTagId"
    }
}
