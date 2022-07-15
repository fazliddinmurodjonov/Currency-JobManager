package com.example.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.room.entity.Currency
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CurrencyDao {
    @Insert
    fun insertCurrency(currency: Currency)

    @Update
    fun updateCurrency(currency: Currency)

    @Query("select *from Currency where id = :id")
    fun getById(id: Int): Currency

    @Query("select *from Currency where Ccy = :Ccy")
    fun getByCcy(Ccy: String): Currency

    @Query("select *from Currency")
    fun getAllCurrency(): List<Currency>

    @Query("select *from Currency")
    fun getAllCurrencyFlowable(): Flowable<List<Currency>>
}