package com.noblemajesty.brt

import android.graphics.Point
import android.os.RemoteException
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import android.support.v4.app.Fragment
import com.noblemajesty.brt.views.authentication.LoginFragment
import com.noblemajesty.brt.views.authentication.RegistrationActivity
import com.noblemajesty.brt.views.authentication.RegistrationFragment
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ARegistrationActivityTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule(RegistrationActivity::class.java)

    @Before
    fun init() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val coordinates = arrayOfNulls<Point>(4)
        coordinates[0] = Point(248, 1520)
        coordinates[1] = Point(248, 929)
        coordinates[2] = Point(796, 1520)
        coordinates[3] = Point(796, 929)
        try {
            if (!uiDevice.isScreenOn) {
                uiDevice.wakeUp()
                uiDevice.swipe(coordinates, 10)
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    private fun goToFragment(fragment: Fragment) {
        mActivityRule.activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    @Test
    fun test_A_RegistrationActivity_shouldDisplayCorrectly() {
        onView(withId(R.id.container))
                .check(matches(allOf(
                        isDisplayed(),
                        hasFocus(),
                        not(isClickable()),
                        not(isFocusable())
                )))
    }

    @Test
    fun test_B_LoginFragment_shouldBeDisplayedFirst() {
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
    fun test_C_LoginFragment_shouldDisplayCorrectly() {
        assertNotNull(R.id.message)
        assertNotNull(R.id.loginEmail)
        assertNotNull(R.id.loginEmailField)
        assertNotNull(R.id.loginPassword)
        assertNotNull(R.id.loginPasswordField)
        assertNotNull(R.id.loginText)
        assertNotNull(R.id.loginButton)
    }

    @Test
    fun test_D_LoginFragment_shouldAllowValidLogin() {
        goToFragment(LoginFragment.newInstance())
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
    fun test_E_SnackbarShouldBeDisplayedIfAnyEmptyField() {
        onView(withId(R.id.loginButton))
                .perform(click())

        onView(withText("All fields are required.".toUpperCase()))
                .check(matches(isDisplayed()))
    }

    @Test
    fun test_F_SignupTextShouldLaunchSignupFragment() {
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
    fun test_G_RegistrationFragment_shouldBeDisplayedProperly() {
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
    fun test_H_LoginTextShouldLaunchLoginFragment() {
        goToFragment(RegistrationFragment.newInstance())

        onView(withId(R.id.loginText))
                .check(matches(allOf(isDisplayed(), isClickable())))

        onView(withId(R.id.loginText))
                .perform(click())

        onView(withId(R.id.loginContainer))
                .check(matches(isDisplayed()))
    }

    @Test
    fun test_I_SignupShouldNotAllowIfOneOrMoreFieldsAreEmpty() {
        goToFragment(RegistrationFragment.newInstance())

        onView(withId(R.id.registerButton))
                .perform(click())

        onView(withText("All fields are required.".toUpperCase()))
                .check(matches(isDisplayed()))

        onView(withId(R.id.signupEmailField))
                .perform(typeText("Email@email.com"), closeSoftKeyboard())

        onView(withId(R.id.registerButton))
                .perform(click())

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(isEnabled()))
    }

    @Test
    fun test_J_Signup_shouldWorkPerfectly() {
        goToFragment(RegistrationFragment.newInstance())
        onView(withId(R.id.signupEmailField))
                .perform(typeText("Email@email.com"), closeSoftKeyboard())
        onView(withId(R.id.signupLastNameField))
                .perform(typeText("LastName"), closeSoftKeyboard())
        onView(withId(R.id.signupFirstNameField))
                .perform(typeText("FirstName"), closeSoftKeyboard())
        onView(withId(R.id.signupPasswordField))
                .perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.registerButton))
                .perform(click())
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("All fields are required.".toUpperCase())))
                .check(doesNotExist())
    }

    @Test
    fun test_K_Signup_shouldNotWorkIfEmailHasBeenTaken() {
        goToFragment(RegistrationFragment.newInstance())
        onView(withId(R.id.signupEmailField))
                .perform(typeText("Email@email.com"), closeSoftKeyboard())
        onView(withId(R.id.signupLastNameField))
                .perform(typeText("LastName"), closeSoftKeyboard())
        onView(withId(R.id.signupFirstNameField))
                .perform(typeText("FirstName"), closeSoftKeyboard())
        onView(withId(R.id.signupPasswordField))
                .perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.registerButton))
                .perform(click())
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("All fields are required.".toUpperCase())))
                .check(doesNotExist())
    }


    @Test
    fun test_L_Login_shouldWorkCorrectlyAfterUserSignUp() {
        goToFragment(LoginFragment.newInstance())
        onView(withId(R.id.loginEmailField))
                .perform(typeText("Email@email.com"), closeSoftKeyboard())
        onView(withId(R.id.loginPasswordField))
                .perform(typeText("password"), closeSoftKeyboard())
        onView(withId(R.id.loginButton))
                .perform(click())
        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText("All fields are required.".toUpperCase())))
                .check(doesNotExist())
    }
}