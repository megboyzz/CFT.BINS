package ru.megboyzz.bin.db

import androidx.room.Database
import androidx.room.RoomDatabase
/*
@Database(
    entities = null,
    version = 1
)*/
abstract class DataBase : RoomDatabase(){
    //abstract fun girlDao(): GirlDao
}