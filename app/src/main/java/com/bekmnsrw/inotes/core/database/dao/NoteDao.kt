package com.bekmnsrw.inotes.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.bekmnsrw.inotes.core.database.entity.Note
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor

@Dao
interface NoteDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(note: Note)

    @Query("UPDATE note SET " +
            "title = :title, " +
            "content = :content, " +
            "last_modified = :lastModified, " +
            "is_pinned = :isPinned, " +
            "card_color = :cardColor," +
            "tag_id = :tagId " +
            "WHERE id = :id"
    )
    suspend fun update(
        id: Long,
        title: String?,
        content: String,
        lastModified: Long,
        isPinned: Boolean,
        cardColor: CardColor,
        tagId: Long
    )

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun findById(id: Long): Note

    @Query("SELECT * FROM note ORDER BY is_pinned DESC, last_modified DESC")
    suspend fun findAll(): List<Note>
}
