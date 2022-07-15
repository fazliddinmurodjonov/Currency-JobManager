package com.example.androidconnectionslesson7currency

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.adapter.CurrencyAdapter
import com.example.androidconnectionslesson7currency.databinding.ActivityMainBinding
import com.example.androidconnectionslesson7currency.databinding.CustomDialogCurrencyBinding
import com.example.retrofit.Common
import com.example.retrofit.CurrencyRetrofit
import com.example.retrofit.RetrofitServices
import com.example.room.database.CurrencyDB
import com.example.room.entity.Currency
import com.example.service.UploadCurrency
import com.example.utils.SharedPreference
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding()
    var currencyAdapter = CurrencyAdapter()
    val db = CurrencyDB.getInstance(App.instance)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreference.init(this)
        if (SharedPreference.uploadMode == 0) {
            SharedPreference.uploadMode = 1
            uploadWorkManager()
        }

        db.currencyDao().getAllCurrencyFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@SuppressLint("NewApi")
            object : Consumer<List<Currency>>,
                io.reactivex.rxjava3.functions.Consumer<List<Currency>> {
                override fun accept(t: List<Currency>) {
                    currencyAdapter.submitList(t)
                }
            },
                @SuppressLint("NewApi")
                object : Consumer<Throwable>,
                    io.reactivex.rxjava3.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }
                })
        binding.currenciesRV.adapter = currencyAdapter

        currencyAdapter.setOnItemClickListener { currency ->
            val dialog = Dialog(this)
            val dialogView =
                CustomDialogCurrencyBinding.inflate(layoutInflater)
            dialog.setContentView(dialogView.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogView.currencyTv.text = "Currency      : 1 ${currency.CcyNm_EN}"
            dialogView.symbolCodeTv.text = "Symbol code   : ${currency.Ccy}"
            dialogView.numberCodeTv.text = "Number code   : ${currency.Code}"
            dialogView.changeTv.text = "Change        : ${currency.Diff}"
            dialogView.exchangeRateTv.text = "Exchange rate : ${currency.Rate}"
            dialogView.dateTv.text = "Date          : ${currency.Date}"
            dialog.show()
        }


    }

    private fun uploadWorkManager() {
        val uploadCurrencyRequest: WorkRequest =
            PeriodicWorkRequestBuilder<UploadCurrency>(15, TimeUnit.MINUTES)
                .build()
        WorkManager
            .getInstance(this)
            .enqueue(uploadCurrencyRequest)
    }
}