package com.bekmnsrw.inotes.core.database.mapper

import com.bekmnsrw.inotes.core.database.entity.Note
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto

fun NoteDto.toNote(): Note = Note(
    id = id,
    title = title,
    content = content,
    lastModified = lastModified,
    isPinned = isPinned,
    cardColor = cardColor,
    tagId = tagId
)

fun Note.toNoteDto(): NoteDto = NoteDto(
    id = id,
    title = title,
    content = content,
    lastModified = lastModified,
    isPinned = isPinned,
    cardColor = cardColor,
    tagId = tagId
)

fun List<Note>.toNoteDtoList(): List<NoteDto> = this.map { it.toNoteDto() }
