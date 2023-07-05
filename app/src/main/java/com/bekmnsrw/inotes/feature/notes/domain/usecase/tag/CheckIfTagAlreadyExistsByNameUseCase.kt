package com.bekmnsrw.inotes.feature.notes.domain.usecase.tag

import com.bekmnsrw.inotes.feature.notes.domain.TagRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckIfTagAlreadyExistsByNameUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(
        tagName: String
    ): Flow<Boolean> = tagRepository.checkIfTagAlreadyExistsByName(tagName)
}
