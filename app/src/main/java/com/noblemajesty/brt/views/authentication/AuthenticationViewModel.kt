package com.noblemajesty.brt.views.authentication

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.entities.User

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {

    var errorMessage = ""
    var firstName = ""
    var lastName = ""
    var email = ""
    var userId = 0

    fun loginUser(email: String, password: String): Boolean {
        var isRegisteredUser = false
        val user = BRTDatabase.getDatabaseInstance(getApplication())
                .userDAO().findUserByEmail(email)
        user?.let {
            if (it.password == password) isRegisteredUser = true
            this.firstName = it.firstName
            this.lastName = it.lastName
            this.email = it.email
            this.userId = it.userId!!
            Log.e("Logged in User", "$it")
        }
        errorMessage = "Invalid Login details"
        return isRegisteredUser
    }

    fun signup(firstName: String, lastName: String, email: String, password: String): Boolean {
        var isRegisteredUser = false

        val userWithEmail = BRTDatabase.getDatabaseInstance(getApplication())
                .userDAO().findUserByEmail(email)
        if (userWithEmail != null) {
            errorMessage = "Email already Taken"
            return false
        }

        val user = User(firstName = firstName, lastName = lastName, email = email, password = password)
        val userId = BRTDatabase.getDatabaseInstance(getApplication())
                .userDAO().createUser(user)

        userId?.let {
            isRegisteredUser = true
            Log.e("Registered UserId", "$it")
        }
        return isRegisteredUser
    }
}
