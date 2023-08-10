package tgs.app.storyapp.preference

import android.content.Context
import tgs.app.storyapp.model.LoginResult

internal class UserPreference(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: LoginResult) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): LoginResult {
        val model = LoginResult()
        model.token = preferences.getString(TOKEN, "")
        return model
    }

    fun deleteUser() {
        val editor = preferences.edit().clear()
        editor.apply()
    }

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
    }
}