package com.bekmnsrw.inotes.feature.notes.domain.usecase.tag

import com.bekmnsrw.inotes.feature.notes.domain.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IncreaseNoteCountUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(
        id: Long
    ): Flow<Unit> = tagRepository.increaseNoteCount(id)
}
