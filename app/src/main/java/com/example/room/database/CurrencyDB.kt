package com.example.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.room.dao.CurrencyDao
import com.example.room.entity.Currency


@Database(entities = [Currency::class], version = 1)
abstract class CurrencyDB : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        private var instance: CurrencyDB? = null

        @Synchronized
        fun getInstance(context: Context): CurrencyDB {
            if (instance == null) {
                instance = Room.databaseBuilder(context, CurrencyDB::class.java, "currency_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}