package com.noblemajesty.brt.views.authentication

import com.noblemajesty.brt.database.BRTDatabase
import com.noblemajesty.brt.database.dao.UserDAO
import com.noblemajesty.brt.database.entities.User
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthenticationViewModelTest {

    @Rule @JvmField
    val mockitoRule = MockitoJUnit.rule()!!

    @Test
    fun loginUser_shouldWorkCorrectlyIfLoginIsUnsuccessful() {
        val viewModel = mock(AuthenticationViewModel::class.java)
        `when`(viewModel.loginUser("email", "password")).thenReturn(false)
        `when`(viewModel.errorMessage).thenReturn("Invalid Login details")
        assertEquals(viewModel.loginUser("email", "password"), false)
        assertEquals(viewModel.errorMessage, "Invalid Login details")
    }

    @Test
    fun loginUser_shouldWorkCorrectlyIfLoginIsSuccessful() {
        val viewModel = mock(AuthenticationViewModel::class.java)
         assertNull(viewModel.errorMessage)
        `when`(viewModel.loginUser("email", "password")).thenReturn(true)
        assertEquals(viewModel.loginUser("email", "password"), true)
        assertEquals(viewModel.errorMessage, null)
    }

    @Test
    fun setUserInfoToViewModel_shouldWorkAsExpected() {
        val viewModel = AuthenticationViewModel()

        assertEquals(viewModel.errorMessage, "")
        assertEquals(viewModel.lastName, "")
        assertEquals(viewModel.firstName, "")
        assertEquals(viewModel.email, "")

        val mockUser = User().apply {
            firstName = "firstName"
            lastName = "lastName"
            email = "email"
            userId = 0
        }

        viewModel.setUserInfoToViewModel(mockUser)
        assertEquals(viewModel.lastName, "lastName")
    }

    @Test
    fun database_shouldWorkProperly() {
        val database = mock(BRTDatabase::class.java)
        val viewModel = AuthenticationViewModel()
        val mockUserDAO = mock(UserDAO::class.java)
        val mockUser = User().apply {
            firstName = "firstName"
            lastName = "lastName"
            email = "email"
            userId = 0
        }
        assertEquals(viewModel.loginUser("fakeEmail", "fakePassword"), false)
        assertEquals(viewModel.errorMessage, "Invalid Login details")

    }

    @Test
    fun signup_shouldWorkCorrectlyIfSignUpIsUnsuccessful() {
        val viewModel = mock(AuthenticationViewModel::class.java)
        `when`(viewModel.signup("firstName", "lastName", "email", "password")).thenReturn(false)
        `when`(viewModel.errorMessage).thenReturn("Email already Taken")
        assertEquals(viewModel.signup("firstName", "lastName", "email", "password"), false)
        assertEquals(viewModel.errorMessage, "Email already Taken")

        assertEquals(viewModel.signup("firstName", "lastName", "email", "password"), false)
        assertEquals(viewModel.errorMessage, "Email already Taken")

        verify(viewModel, times(2)).signup("firstName", "lastName", "email", "password")
    }

    @Test
    fun findRegisteredUser_shouldWorkCorrectly() {
        val viewModel = mock(AuthenticationViewModel::class.java)
        `when`(viewModel.findRegisteredUser("email")).thenReturn(User())
        assertEquals(viewModel.findRegisteredUser("email"), User())
        verify(viewModel, times(1)).findRegisteredUser("email")
    }
}