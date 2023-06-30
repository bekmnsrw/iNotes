package com.bekmnsrw.inotes.feature.notes.domain.usecase.note

import com.bekmnsrw.inotes.feature.notes.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateIsPinnedUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(
        id: Long,
        isPinned: Boolean
    ): Flow<Boolean> = noteRepository.updateIsPinned(
        id = id,
        isPinned = isPinned
    )
}
