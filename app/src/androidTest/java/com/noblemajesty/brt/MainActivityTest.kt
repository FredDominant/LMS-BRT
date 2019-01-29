package com.noblemajesty.brt

import android.graphics.Point
import android.os.RemoteException
import android.support.test.InstrumentationRegistry

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.contrib.DrawerMatchers.isOpen
import android.support.test.espresso.contrib.NavigationViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.support.v4.app.Fragment
import android.view.Gravity
import com.noblemajesty.brt.views.home.MainActivity
import com.noblemajesty.brt.views.home.ProfileFragment
import com.noblemajesty.brt.views.home.RecentSchedulesFragment
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import org.junit.ClassRule



@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    companion object {
        @ClassRule @JvmField
        var animationsRule = DisableAnimationsRule()
    }

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
                .replace(R.id.frameContainer, fragment)
                .commit()
    }

    @Test
    fun test_A_RecentScheduleFragments_shouldBeDisplayedFirst() {
        val currentFragment = mActivityRule
                .activity
                .supportFragmentManager.findFragmentById(R.id.frameContainer)

        assertTrue(currentFragment is RecentSchedulesFragment)
    }

    @Test
    fun test_B_RecentScheduleFragments_shouldHaveNoRecentScheduleOnFirstLogin() {
        goToFragment(RecentSchedulesFragment.newInstance())
        onView(withId(R.id.noRecentScheduleText))
                .check(matches(isDisplayed()))
    }

    @Test
    fun mainActivity_shouldDisplayCorrectly() {
        Assert.assertNotNull(R.id.drawer_layout)
        Assert.assertNotNull(R.id.nav_view)

        onView(withId(R.id.drawer_layout))
                .perform(click())
        onView(withId(R.id.drawer_layout))
                .check(matches(allOf(isDisplayed())))
    }

    @Test
    fun drawer_shouldWorkCorrectly() {
        Thread.sleep(1000)
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.profile))

        onView(withId(R.id.profileContainer))
                .check(matches(isDisplayed()))

        Thread.sleep(1000)
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.recentSchedules))

        onView(withId(R.id.recentSchedulesContainer))
                .check(matches(isDisplayed()))

        Thread.sleep(1000)
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.addSchedule))

        onView(withId(R.id.addScheduleContainer))
                .check(matches(isDisplayed()))

    }
}