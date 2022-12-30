package ru.megboyzz.bin.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.megboyzz.bin.entity.BINInfo

class BINConverter {

    @TypeConverter
    fun fromBIN(info: BINInfo): String{
        return Gson().toJson(info)
    }

    @TypeConverter
    fun toBIN(data: String): BINInfo{
        return Gson().fromJson(data, BINInfo::class.java)
    }

}