package com.bekmnsrw.inotes.feature.notes.domain

import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun save(noteDto: NoteDto): Flow<Unit>
    suspend fun findAll(): Flow<List<NoteDto>>
    suspend fun findById(id: Long): Flow<NoteDto>
}
