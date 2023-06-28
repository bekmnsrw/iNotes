package com.bekmnsrw.inotes.core.database.di

import android.content.Context
import androidx.room.Room
import com.bekmnsrw.inotes.core.database.AppDatabase
import com.bekmnsrw.inotes.core.database.converter.CardColorConverter
import com.bekmnsrw.inotes.core.database.dao.NoteDao
import com.bekmnsrw.inotes.core.database.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    companion object {
        private const val DB_NAME = "iNoteDb"
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        appContext: Context,
        cardColorConverter: CardColorConverter
    ): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        DB_NAME
    )
        .addTypeConverter(cardColorConverter)
        .build()

    @Provides
    fun provideCardColorConverter(): CardColorConverter = CardColorConverter()

    @Provides
    fun provideNoteDao(
        appDatabase: AppDatabase
    ): NoteDao = appDatabase.noteDao()

    @Provides
    fun provideTagDao(
        appDatabase: AppDatabase
    ): TagDao = appDatabase.tagDao()
}
