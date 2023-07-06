package com.bekmnsrw.inotes.feature.notes.domain

import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun save(noteDto: NoteDto): Flow<Long>
    suspend fun findAll(): Flow<List<NoteDto>>
    suspend fun findById(id: Long): Flow<NoteDto>
    suspend fun updateIsPinned(id: Long, isPinned: Boolean): Flow<Boolean>
    suspend fun updateCardColor(id: Long, cardColor: CardColor): Flow<Unit>
    suspend fun findAllByTagId(tagId: Long): Flow<List<NoteDto>>
}
