package com.bekmnsrw.inotes.feature.notes.data

import com.bekmnsrw.inotes.core.database.AppDatabase
import com.bekmnsrw.inotes.core.database.mapper.toNote
import com.bekmnsrw.inotes.core.database.mapper.toNoteDto
import com.bekmnsrw.inotes.core.database.mapper.toNoteDtoList
import com.bekmnsrw.inotes.feature.notes.domain.NoteRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : NoteRepository {

    override suspend fun save(noteDto: NoteDto): Flow<Long> {
        return flowOf(
            appDatabase.noteDao()
                .save(note = noteDto.toNote())
        )
    }

    override suspend fun findAll(): Flow<List<NoteDto>> {
        return appDatabase.noteDao()
            .findAll()
            .map { it.toNoteDtoList() }
    }

    override suspend fun findById(id: Long): Flow<NoteDto> {
        return flowOf(
            appDatabase.noteDao()
                .findById(id = id)
                .toNoteDto()
        )
    }

    override suspend fun updateIsPinned(
        id: Long,
        isPinned: Boolean
    ): Flow<Boolean> {
        appDatabase.noteDao()
            .updateIsPinned(
                id = id,
                isPinned = isPinned
            )

        return flowOf(isPinned)
    }

    override suspend fun updateCardColor(
        id: Long,
        cardColor: CardColor
    ): Flow<Unit> {
        return flowOf(
            appDatabase.noteDao()
                .updateCardColor(
                    id = id,
                    cardColor = cardColor
                )
        )
    }
}
