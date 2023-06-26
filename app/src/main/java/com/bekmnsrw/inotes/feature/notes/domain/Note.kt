package com.bekmnsrw.inotes.feature.notes.domain

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val isPinned: Boolean,
    val lastModified: String,
    val cardColor: Long
)
