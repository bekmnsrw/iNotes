package com.bekmnsrw.inotes.feature.notes.domain.usecase.note

import com.bekmnsrw.inotes.feature.notes.domain.NoteRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCardColorUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(
        id: Long,
        cardColor: CardColor
    ): Flow<Unit> = noteRepository.updateCardColor(
        id = id,
        cardColor = cardColor
    )
}
