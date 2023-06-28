package com.bekmnsrw.inotes.feature.notes.di

import com.bekmnsrw.inotes.core.database.AppDatabase
import com.bekmnsrw.inotes.feature.notes.data.NoteRepositoryImpl
import com.bekmnsrw.inotes.feature.notes.data.TagRepositoryImpl
import com.bekmnsrw.inotes.feature.notes.domain.NoteRepository
import com.bekmnsrw.inotes.feature.notes.domain.TagRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module(includes = [BindNoteRepository::class, BindTagRepository::class])
@InstallIn(ActivityComponent::class)
class NoteModule {

    @Provides
    fun provideNoteRepository(
        appDatabase: AppDatabase
    ): NoteRepository = NoteRepositoryImpl(appDatabase)

    @Provides
    fun provideTagRepository(
        appDatabase: AppDatabase
    ): TagRepository = TagRepositoryImpl(appDatabase)
}

@Module
@InstallIn(SingletonComponent::class)
interface BindNoteRepository {

    @Binds
    fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface BindTagRepository {

    @Binds
    fun bindTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository
}
