package ru.megboyzz.bin.entity

import androidx.room.Entity

@Entity
data class Bank(
    val name: String,
    val url: String,
    val phone: String,
    val city: String
)
