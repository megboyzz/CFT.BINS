package ru.megboyzz.bin

import android.app.Application
import androidx.room.Room
import ru.megboyzz.bin.db.DataBase

class App : Application() {
    var database: DataBase? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, DataBase::class.java, "database")
            .build()
    }

    companion object {
        var instance: App? = null
    }
}