package com.bekmnsrw.inotes.core.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bekmnsrw.inotes.feature.notes.domain.dto.CardColor

@ProvidedTypeConverter
class CardColorConverter {

    @TypeConverter
    fun CardColorToString(cardColor: CardColor): String = cardColor.name

    @TypeConverter
    fun StringToCardColor(string: String): CardColor = CardColor.valueOf(string)
}
