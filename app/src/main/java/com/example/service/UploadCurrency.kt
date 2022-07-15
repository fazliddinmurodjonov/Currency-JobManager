package com.example.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.androidconnectionslesson7currency.App
import com.example.retrofit.Common
import com.example.retrofit.CurrencyRetrofit
import com.example.room.database.CurrencyDB
import com.example.room.entity.Currency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadCurrency(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        uploadCurrencies()
        return Result.success()
    }

    private fun uploadCurrencies() {
        val db = CurrencyDB.getInstance(App.instance)
        var retrofitService = Common.retrofitService
        retrofitService.getCurrencies().enqueue(object : Callback<List<CurrencyRetrofit>> {
            override fun onResponse(
                call: Call<List<CurrencyRetrofit>>,
                response: Response<List<CurrencyRetrofit>>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()
                    if (db.currencyDao().getAllCurrency().isEmpty()) {
                        list?.forEach { c ->
                            val currency = Currency(c.Ccy,
                                c.CcyNm_EN,
                                c.CcyNm_RU,
                                c.CcyNm_UZ,
                                c.CcyNm_UZC,
                                c.Code,
                                c.Date,
                                c.Diff,
                                c.Nominal,
                                c.Rate)
                            db.currencyDao().insertCurrency(currency)
                        }
                    } else {
                        list?.forEach { c ->
                            val currencyByCcy = db.currencyDao().getByCcy(c.Ccy)
                            currencyByCcy.Rate = c.Rate
                            currencyByCcy.Diff = c.Diff
                            currencyByCcy.Date = c.Date
                            db.currencyDao().updateCurrency(currencyByCcy)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<CurrencyRetrofit>>, t: Throwable) {
            }

        })
    }
}
