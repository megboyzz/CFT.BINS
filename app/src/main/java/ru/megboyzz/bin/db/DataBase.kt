package ru.megboyzz.bin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.megboyzz.bin.db.dao.BINInfoDao
import ru.megboyzz.bin.entity.BINInfoNumber

@Database(
    entities = [BINInfoNumber::class],
    version = 1
)
abstract class DataBase : RoomDatabase(){
    abstract fun binInfoDao(): BINInfoDao
}