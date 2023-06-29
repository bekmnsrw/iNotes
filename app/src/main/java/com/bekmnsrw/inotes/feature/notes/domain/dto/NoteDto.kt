package com.bekmnsrw.inotes.feature.notes.domain.dto

data class NoteDto(
    val id: Long,
    val title: String?,
    val content: String,
    val isPinned: Boolean,
    val lastModified: Long,
    val cardColor: CardColor,
    val tagId: Long
)
