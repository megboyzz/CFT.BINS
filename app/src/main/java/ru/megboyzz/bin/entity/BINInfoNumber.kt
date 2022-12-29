package ru.megboyzz.bin.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BINInfoNumber(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val number: Int,
    val info: BINInfo //TODO Сделать typeConverter
)
