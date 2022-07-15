package com.example.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidconnectionslesson7currency.App
import com.example.androidconnectionslesson7currency.databinding.ItemCurrencyBinding
import com.example.room.entity.Currency
import kotlin.collections.ArrayList

class CurrencyAdapter : ListAdapter<Currency, CurrencyAdapter.CurrencyViewHolder>(MyDiffUtil()) {
    lateinit var currencyAdapter: OnItemClick

    fun interface OnItemClick {
        fun onClick(currency: Currency)
    }

    fun setOnItemClickListener(listener: OnItemClick) {
        currencyAdapter = listener
    }

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currency: Currency) {
            val imageId = App.instance.resources.getIdentifier(currency.Ccy?.lowercase(),
                "drawable",
                App.instance.packageName)
            binding.countryFlag.setImageResource(imageId)
            binding.currencyName.text = currency.CcyNm_EN
            binding.root.setOnClickListener {
                currencyAdapter.onClick(currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return when {
                oldItem.Diff != newItem.Diff -> {
                    false
                }
                else -> true
            }
        }

    }

}