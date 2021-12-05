package com.stu.quest.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult (
     val success:LoggedInUserView? = null,
     val error:Int? = null,
     val errorMessage:String? = ""
)