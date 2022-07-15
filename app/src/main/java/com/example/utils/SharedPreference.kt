package com.example.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val NAME = "CurrencyApp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreference: SharedPreferences
    fun init(context: Context) {
        sharedPreference = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var uploadMode: Int?
        get() = sharedPreference.getInt("upload", 0)
        set(value) = sharedPreference.edit {
            it.putInt("upload", value!!)
        }

}