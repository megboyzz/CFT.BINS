package ru.megboyzz.bin.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.megboyzz.bin.converter.BINConverter

@Entity
@TypeConverters(BINConverter::class)
data class BINInfoNumber(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val number: Int,
    val info: BINInfo //TODO Сделать typeConverter
)
