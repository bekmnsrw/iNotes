package com.bekmnsrw.inotes.feature.notes.data

import com.bekmnsrw.inotes.core.database.AppDatabase
import com.bekmnsrw.inotes.core.database.mapper.toTag
import com.bekmnsrw.inotes.core.database.mapper.toTagDto
import com.bekmnsrw.inotes.core.database.mapper.toTagDtoList
import com.bekmnsrw.inotes.feature.notes.domain.TagRepository
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : TagRepository {

    override suspend fun getAll(): Flow<List<TagDto>> = flow {
        emit(
            appDatabase.tagDao()
                .findAll()
                .toTagDtoList()
        )
    }

    override suspend fun getById(id: Long): Flow<TagDto?> = flow {
        emit(
            appDatabase.tagDao()
                .findById(id)
                .toTagDto()
        )
    }

    override suspend fun save(tagDto: TagDto): Flow<Unit> {
        appDatabase.tagDao()
            .save(tagDto.toTag())

        return flowOf(Unit)
    }

    override suspend fun isTagExists(id: Long): Flow<Boolean> = flow {
        emit(
            appDatabase.tagDao()
                .isTagExists(id)
        )
    }
}
