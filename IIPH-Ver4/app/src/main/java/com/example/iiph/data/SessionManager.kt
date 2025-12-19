package com.example.iiph.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences("IIPH_APP_PREFS", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val CURRENT_YEAR = "current_year"
        const val USER_ID = "user_id"
    }

    fun saveUserSession(currentYear: Int, userId: Int) {
        val editor = prefs.edit()
        // A simple non-null token indicates the user is logged in.
        editor.putString(USER_TOKEN, "LOGGED_IN")
        editor.putInt(CURRENT_YEAR, currentYear)
        editor.putInt(USER_ID, userId)
        editor.apply()
    }

    fun getCurrentYear(): Int {
        return prefs.getInt(CURRENT_YEAR, -1) // Return -1 or some other default if not found
    }

    fun getUserId(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getString(USER_TOKEN, null) != null
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}