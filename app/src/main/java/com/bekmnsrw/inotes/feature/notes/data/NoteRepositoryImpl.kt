package com.bekmnsrw.inotes.feature.notes.data

import com.bekmnsrw.inotes.core.database.AppDatabase
import com.bekmnsrw.inotes.core.database.mapper.toNote
import com.bekmnsrw.inotes.core.database.mapper.toNoteDto
import com.bekmnsrw.inotes.core.database.mapper.toNoteDtoList
import com.bekmnsrw.inotes.feature.notes.domain.NoteRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : NoteRepository {

    override suspend fun save(
        noteDto: NoteDto
    ): Flow<Unit> = flow {
        emit(
            appDatabase.noteDao()
                .save(noteDto.toNote())
        )
    }

    override suspend fun findAll(): Flow<List<NoteDto>> = flow {
        emit(
            appDatabase.noteDao()
                .findAll()
                .toNoteDtoList()
        )
    }

    override suspend fun findById(
        id: Long
    ): Flow<NoteDto> = flow {
        emit(
            appDatabase.noteDao()
                .findById(id)
                .toNoteDto()
        )
    }
}
