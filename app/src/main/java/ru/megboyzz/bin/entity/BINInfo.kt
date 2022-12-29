package ru.megboyzz.bin.entity


data class BINInfo(
    val number: CardNumber,
    val scheme: String,
    val brand: String,
    val prepaid: Boolean,
    val country: CardCountry,
    val bank: Bank
)
