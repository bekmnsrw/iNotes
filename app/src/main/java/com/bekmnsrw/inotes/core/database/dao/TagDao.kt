package com.bekmnsrw.inotes.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.bekmnsrw.inotes.core.database.entity.Tag

@Dao
interface TagDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(tag: Tag)

    @Query("UPDATE tag SET " +
            "name = :name " +
            "WHERE id = :id"
    )
    suspend fun update(
        id: Long,
        name: String
    )

    @Query("DELETE FROM tag WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM tag ORDER BY id ASC")
    suspend fun findAll(): List<Tag>

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun findById(id: Long): Tag

    @Query("SELECT EXISTS(SELECT * FROM tag WHERE id = :id)")
    suspend fun isTagExists(id: Long): Boolean
}
