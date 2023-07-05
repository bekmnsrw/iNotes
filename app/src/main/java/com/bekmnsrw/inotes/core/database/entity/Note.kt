package com.bekmnsrw.inotes.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "last_modified")
    val lastModified: Long,

    @ColumnInfo(
        name = "is_pinned",
        defaultValue = "0"
    )
    val isPinned: Boolean,

    @ColumnInfo(
        name = "card_color",
        defaultValue = "BASE"
    )
    val cardColor: CardColor,

    @ColumnInfo(
        name = "tag_id",
        defaultValue = "1"
    )
    val tagId: Long
)
