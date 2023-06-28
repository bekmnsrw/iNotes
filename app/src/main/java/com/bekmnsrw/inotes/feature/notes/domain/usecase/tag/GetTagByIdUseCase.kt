package com.bekmnsrw.inotes.feature.notes.domain.usecase.tag

import com.bekmnsrw.inotes.feature.notes.domain.TagRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTagByIdUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(
        id: Long
    ): Flow<TagDto?> = tagRepository.getById(id)
}
