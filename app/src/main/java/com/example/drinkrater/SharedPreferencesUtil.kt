package com.example.drinkrater

import android.content.Context
import androidx.activity.ComponentActivity.MODE_PRIVATE
import androidx.core.content.edit

object SharedPreferencesUtil {
    private const val PREFS_NAME = "MyAppPrefs"

    fun saveToken(context: Context, token: String?) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        sharedPreferences.edit() { putString("auth_token", token) }
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit() { remove("token") }
    }

}