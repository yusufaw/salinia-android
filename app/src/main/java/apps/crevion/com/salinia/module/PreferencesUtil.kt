package apps.crevion.com.salinia.module

import android.content.SharedPreferences
import apps.crevion.com.salinia.model.User
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by yusufaw on 2/4/18.
 */

class PreferencesUtil @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun putUserLogin(user: User): Boolean {
        return sharedPreferences.edit().putString("loggedInUser", Gson().toJson(user)).commit()
    }

    fun getUserLogin(): User {
        return Gson().fromJson(sharedPreferences.getString("loggedInUser", ""), User::class.java)
    }
}