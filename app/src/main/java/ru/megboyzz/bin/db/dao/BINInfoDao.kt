package ru.megboyzz.bin.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.megboyzz.bin.entity.BINInfoNumber

@Dao
interface BINInfoDao {

    @Insert
    fun addBinInfo(binInfoNumber: BINInfoNumber)

    @Delete
    fun removeBinInfo(binInfoNumber: BINInfoNumber)

    @Query("SELECT * FROM bininfonumber")
    fun getAll(): List<BINInfoNumber>

}