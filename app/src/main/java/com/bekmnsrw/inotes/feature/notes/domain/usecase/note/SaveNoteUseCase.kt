package com.bekmnsrw.inotes.feature.notes.domain.usecase.note

import com.bekmnsrw.inotes.feature.notes.domain.NoteRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.NoteDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(
        note: NoteDto
    ): Flow<Long> = noteRepository.save(noteDto = note)
}
