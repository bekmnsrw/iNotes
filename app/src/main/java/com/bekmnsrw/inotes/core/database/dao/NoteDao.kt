package com.bekmnsrw.inotes.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bekmnsrw.inotes.core.database.entity.Note
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(note: Note): Long

    @Query("UPDATE note SET is_pinned = :isPinned WHERE id = :id")
    suspend fun updateIsPinned(id: Long, isPinned: Boolean)

    @Query("UPDATE note SET card_color = :cardColor WHERE id = :id")
    suspend fun updateCardColor(id: Long, cardColor: CardColor)

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun findById(id: Long): Note

    @Query("SELECT * FROM note ORDER BY is_pinned DESC, last_modified DESC")
    fun findAll(): Flow<List<Note>>
}
