package com.noblemajesty.brt.views.authentication

import android.arch.lifecycle.ViewModel
import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.entities.User

open class AuthenticationViewModel : ViewModel() {

    open var errorMessage = ""
    open var firstName = ""
    open var lastName = ""
    open var email = ""
    open var userId = 0
    open var database: BRTDatabase? = null

    open fun loginUser(email: String, password: String): Boolean {
        var isRegisteredUser = false
        val user = database?.userDAO()?.findUserByEmail(email)
        user?.let {
            if (it.password == password) isRegisteredUser = true
            setUserInfoToViewModel(it)
        }
        errorMessage = "Invalid Login details"
        return isRegisteredUser
    }

    open fun signup(firstName: String, lastName: String, email: String, password: String): Boolean {
        var isRegisteredUser = false

        val userWithEmail = findRegisteredUser(email)

        if (userWithEmail != null) {
            errorMessage = "Email already Taken"
            return false
        }

        val user = User(firstName = firstName, lastName = lastName, email = email, password = password)
        val userId = database?.userDAO()?.createUser(user)

        userId?.let { isRegisteredUser = true }
        setUserInfoToViewModel(user.apply { this.userId = userId?.toInt() })
        return isRegisteredUser
    }

    open fun setUserInfoToViewModel(user: User) {
        this.firstName = user.firstName
        this.lastName = user.lastName
        this.email = user.email
        this.userId = user.userId!!
    }

    open fun findRegisteredUser(email: String) = database?.userDAO()?.findUserByEmail(email)
}
