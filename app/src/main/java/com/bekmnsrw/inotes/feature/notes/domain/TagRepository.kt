package com.bekmnsrw.inotes.feature.notes.domain

import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    suspend fun getAll(): Flow<List<TagDto>>
    suspend fun getById(id: Long): Flow<TagDto?>
    suspend fun save(tagDto: TagDto): Flow<Unit>
    suspend fun checkIfTagAlreadyExistsById(id: Long): Flow<Boolean>
    suspend fun increaseNoteCount(id: Long): Flow<Unit>
    suspend fun decreaseNoteCount(id: Long): Flow<Unit>
    suspend fun checkIfTagAlreadyExistsByName(tagName: String): Flow<Boolean>
}
