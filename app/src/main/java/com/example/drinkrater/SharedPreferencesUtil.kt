package com.example.drinkrater

import android.content.Context

object SharedPreferencesUtil {
    private const val PREFS_NAME = "MyAppPrefs"

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

}