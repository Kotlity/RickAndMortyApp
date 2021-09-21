package com.example.rickandmortyapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickandmortyapp.converter.Converter

@Database(entities = [Result::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ResultDatabase: RoomDatabase() {

    abstract fun resultDao(): ResultDao

    companion object {
        @Volatile
        var INSTANCE: ResultDatabase? = null
        fun getInstance(context: Context): ResultDatabase {
            var tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, ResultDatabase::class.java, "result_db").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}