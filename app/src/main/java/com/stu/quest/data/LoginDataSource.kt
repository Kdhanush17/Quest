package com.stu.quest.data

import android.content.SharedPreferences
import com.stu.quest.data.model.LoggedInUser
import java.io.IOException
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(val sharedPrefs:SharedPreferences) {

    fun register(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
                if(sharedPrefs.contains(username)){
                   return Result.Error(Exception("Already registered. Please login."))
                }else{
                   sharedPrefs.edit().putString(username, password).apply()
                   return Result.Success<LoggedInUser>(LoggedInUser(username, username))
                }


        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}