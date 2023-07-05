package com.bekmnsrw.inotes.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bekmnsrw.inotes.core.database.converter.CardColorConverter
import com.bekmnsrw.inotes.core.database.dao.NoteDao
import com.bekmnsrw.inotes.core.database.dao.TagDao
import com.bekmnsrw.inotes.core.database.entity.Note
import com.bekmnsrw.inotes.core.database.entity.Tag

@Database(
    entities = [
        Note::class,
        Tag::class
    ],
    version = 1
)
@TypeConverters(CardColorConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
}
