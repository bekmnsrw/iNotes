package com.bekmnsrw.inotes.feature.notes.data

import com.bekmnsrw.inotes.core.database.AppDatabase
import com.bekmnsrw.inotes.core.database.mapper.toTag
import com.bekmnsrw.inotes.core.database.mapper.toTagDto
import com.bekmnsrw.inotes.core.database.mapper.toTagDtoList
import com.bekmnsrw.inotes.feature.notes.domain.TagRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : TagRepository {

    override suspend fun getAll(): Flow<List<TagDto>> {
        return appDatabase.tagDao()
            .findAll()
            .map { it.toTagDtoList() }
    }

    override suspend fun getById(id: Long): Flow<TagDto?> {
        return flowOf(
            appDatabase.tagDao()
                .findById(id)
                .toTagDto()
        )
    }

    override suspend fun save(tagDto: TagDto): Flow<Unit> {
        return flowOf(
            appDatabase.tagDao()
                .save(tagDto.toTag())
        )
    }

    override suspend fun checkIfTagAlreadyExistsById(id: Long): Flow<Boolean> {
        return flowOf(
            appDatabase.tagDao()
                .checkIfTagAlreadyExistsById(id)
        )
    }

    override suspend fun increaseNoteCount(id: Long): Flow<Unit> {
        return flowOf(
            appDatabase.tagDao()
                .increaseNoteCount(id)
        )
    }

    override suspend fun decreaseNoteCount(id: Long): Flow<Unit> {
        return flowOf(
            appDatabase.tagDao()
                .decreaseNoteCount(id)
        )
    }

    override suspend fun checkIfTagAlreadyExistsByName(tagName: String): Flow<Boolean> {
        return flowOf(
            appDatabase.tagDao()
                .checkIfTagAlreadyExistsByName(tagName)
        )
    }
}
