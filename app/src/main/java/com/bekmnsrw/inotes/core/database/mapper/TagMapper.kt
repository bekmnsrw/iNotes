package com.bekmnsrw.inotes.core.database.mapper

import com.bekmnsrw.inotes.core.database.entity.Tag
import com.bekmnsrw.inotes.feature.notes.domain.dto.TagDto

fun Tag.toTagDto(): TagDto = TagDto(
    id = id,
    name = name,
    noteCount = noteCount
)

fun TagDto.toTag(): Tag = Tag(
    id = id,
    name = name,
    noteCount = noteCount
)

fun List<Tag>.toTagDtoList(): List<TagDto> = this.map { it.toTagDto() }
