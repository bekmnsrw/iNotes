package com.bekmnsrw.inotes.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bekmnsrw.inotes.core.database.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(tag: Tag)

    @Query("UPDATE tag SET name = :name WHERE id = :id")
    suspend fun update(id: Long, name: String)

    @Query("DELETE FROM tag WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM tag ORDER BY id ASC")
    fun findAll(): Flow<List<Tag>>

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun findById(id: Long): Tag

    @Query("SELECT EXISTS(SELECT * FROM tag WHERE id = :id)")
    suspend fun checkIfTagAlreadyExistsById(id: Long): Boolean

    @Query("UPDATE tag SET note_count = note_count + 1 WHERE id = :id")
    suspend fun increaseNoteCount(id: Long)

    @Query("UPDATE tag SET note_count = note_count - 1 WHERE id = :id")
    suspend fun decreaseNoteCount(id: Long)

    @Query("SELECT EXISTS(SELECT * FROM tag WHERE name = :tagName)")
    suspend fun checkIfTagAlreadyExistsByName(tagName: String): Boolean
}
