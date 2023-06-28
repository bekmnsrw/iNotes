package com.bekmnsrw.inotes.feature.notes.domain

import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    suspend fun getAll(): Flow<List<TagDto>>
    suspend fun getById(id: Long): Flow<TagDto?>
    suspend fun save(tagDto: TagDto): Flow<Unit>
    suspend fun isTagExists(id: Long): Flow<Boolean>
}
