package com.noblemajesty.brt

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import com.noblemajesty.brt.views.authentication.RegistrationActivity
import com.noblemajesty.brt.views.authentication.RegistrationFragment
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test


@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {
    @Rule @JvmField
    var mActivityRule = ActivityTestRule(RegistrationActivity::class.java)

    private fun goToFragment(fragment: Fragment) {
        mActivityRule.activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    @Test
    fun registrationActivity_shouldDisplayCorrectly() {
        onView(withId(R.id.container))
                .check(matches(allOf(
                        isDisplayed(),
                        hasFocus(),
                        not(isClickable()),
                        not(isFocusable())
                )))
    }

    @Test
    fun loginFragment_shouldBeDisplayedFirst() {
        onView(withId(R.id.loginContainer))
                .check(matches(allOf(
                        isDisplayed(),
                        hasFocus(),
                        not(isClickable()),
                        not(isFocusable())
                )))

        onView(withId(R.id.loginContainer))
                .check(matches(hasChildCount(5)))
    }

    @Test
    fun loginFragment_shouldDisplayCorrectly() {

        assertNotNull(R.id.message)
        assertNotNull(R.id.loginEmail)
        assertNotNull(R.id.loginEmailField)
        assertNotNull(R.id.loginPassword)
        assertNotNull(R.id.loginPasswordField)
        assertNotNull(R.id.loginText)
        assertNotNull(R.id.loginButton)
    }

    @Test
    fun loginFragment_shouldAllowValidLogin() {
        onView(withId(R.id.loginEmailField))
                .perform(typeText("email@email.com"), closeSoftKeyboard())

        onView(withId(R.id.loginPasswordField))
                .perform(typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.loginButton))
                .perform(click())
        onView(withText("All fields are required.".toUpperCase()))
                .check(doesNotExist())
    }

    @Test
    fun snackbarShouldBeDisplayedIfAnyEmptyField() {
        onView(withId(R.id.loginButton))
                .perform(click())

        onView(withText("All fields are required.".toUpperCase()))
                .check(matches(isDisplayed()))
    }

    @Test
    fun signupTextShouldLaunchSignupFragment() {
        onView(withId(R.id.signupText))
                .check(matches(allOf(
                        isDisplayed(),
                        isClickable()
                )))

        onView(withId(R.id.signupText))
                .perform(click())

        onView(withId(R.id.registrationParent))
                .check(matches(allOf(isDisplayed(), hasChildCount(7))))
    }

    @Test
    fun registrationFragment_shouldBeDisplayedProperly() {
        goToFragment(RegistrationFragment.newInstance())

        onView(withId(R.id.registrationParent))
                .check(matches(allOf(isDisplayed(), hasChildCount(7))))

        assertNotNull(R.id.message)
        assertNotNull(R.id.firstName)
        assertNotNull(R.id.lastName)
        assertNotNull(R.id.email)
        assertNotNull(R.id.password)
        assertNotNull(R.id.loginText)
        assertNotNull(R.id.registerButton)
    }

    @Test
    fun loginTextShouldLaunchLoginFragment() {
        goToFragment(RegistrationFragment.newInstance())

        onView(withId(R.id.loginText))
                .check(matches(allOf(isDisplayed(), isClickable())))

        onView(withId(R.id.loginText))
                .perform(click())

        onView(withId(R.id.loginContainer))
                .check(matches(isDisplayed()))
    }

    @Test
    fun signupShouldNotAllowIfOneOrMoreFieldsAreEmpty() {
        goToFragment(RegistrationFragment.newInstance())

        onView(withId(R.id.registerButton))
                .perform(click())

        onView(withText("All fields are required.".toUpperCase()))
                .check(matches(isDisplayed()))

        onView(withId(R.id.signupEmailField))
                .perform(typeText("Email@email.com"), closeSoftKeyboard())

        onView(withId(R.id.registerButton))
                .perform(click())

        onView(withText("All fields are required.".toUpperCase()))
                .check(matches(isDisplayed()))

    }
}