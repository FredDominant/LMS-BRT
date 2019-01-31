package com.noblemajesty.brt

import android.graphics.Point
import android.os.RemoteException
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerActions
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.contrib.NavigationViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import android.support.v4.app.Fragment
import android.view.Gravity
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import com.noblemajesty.brt.adapters.BusScheduleViewHolder
import com.noblemajesty.brt.adapters.BusSeatViewHolder
import com.noblemajesty.brt.adapters.BusViewHolder
import com.noblemajesty.brt.views.home.AvailableBusesFragment
import com.noblemajesty.brt.views.home.MainActivity
import com.noblemajesty.brt.views.home.ProfileFragment
import com.noblemajesty.brt.views.home.RecentSchedulesFragment
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.allOf
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {

    @Rule @JvmField
    var mActivityRule = ActivityTestRule(MainActivity::class.java)
    private lateinit var uiDevice: UiDevice

    companion object {
        @ClassRule @JvmField
        var animationsRule = DisableAnimationsRule()
    }

    @Before
    fun init() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
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

    private fun fillSchedulesWithValidData(date: String, time: String) {
        onView(withId(R.id.tripFromField))
                .perform(clearText(), typeText("Lagos"), closeSoftKeyboard())

        onView(withId(R.id.tripToField))
                .perform(clearText(), typeText("Kampala"), closeSoftKeyboard())

        onView(withId(R.id.departureDateField))
                .perform(typeText(date))

        Thread.sleep(2000)
        uiDevice.findObject(UiSelector().text("OK")).click()

        onView(withId(R.id.departureTimeField))
                .perform(typeText(time))

        Thread.sleep(2000)
        uiDevice.findObject(UiSelector().text("OK")).click()

//        onView(withId(R.id.departureDateField))
//                .perform(PickerActions.setDate(2030, 4, 22))

//        uiDevice.findObject(UiSelector().text("OK")).click()
//        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
//                .perform(clearText(),
//                        PickerActions.setDate(2040, 11, 4),
//                        closeSoftKeyboard())

//        onView(withId(R.id.departureTimeField))
//                .perform(clearText(),
//                        PickerActions.setTime(10, 25),
//                        closeSoftKeyboard())
        Thread.sleep(2000)

    }

    private fun confirmSuccessfulSeatBooking() {
        onView(withId(R.id.busSeatList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<BusSeatViewHolder>(0, click()))

        Thread.sleep(1000)

        uiDevice.findObject(UiSelector().text("CANCEL")).click()

        Thread.sleep(1000)

        onView(withId(R.id.busSeatList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<BusSeatViewHolder>(0, click()))

        Thread.sleep(1000)

        uiDevice.findObject(UiSelector().text("CONFIRM")).click()
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

    @Test
    fun profileFragment_shouldWorkCorrectly() {
        goToFragment(ProfileFragment.newInstance())

        Thread.sleep(2000)

        onView(withId(R.id.profileFirstNameField))
                .perform(clearText(), typeText("  "), closeSoftKeyboard())

        onView(withId(R.id.profileLastNameField))
                .perform(clearText(), typeText("  "), closeSoftKeyboard())

        onView(withId(R.id.profileEmailField))
                .perform(clearText(), typeText("  "), closeSoftKeyboard())

        onView(withId(R.id.profilePasswordField))
                .perform(clearText(), typeText("  "), closeSoftKeyboard())

        onView(withId(R.id.updateProfile))
                .perform(click())

        onView(withText("All Fields are Required".toUpperCase()))
                .check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(R.id.profileFirstNameField))
                .perform(clearText(), typeText("updated firstName"), closeSoftKeyboard())

        onView(withId(R.id.profileLastNameField))
                .perform(clearText(), typeText("updated lastName"), closeSoftKeyboard())

        onView(withId(R.id.profileEmailField))
                .perform(clearText(), typeText("updatedEmail@email.com"), closeSoftKeyboard())

        onView(withId(R.id.profilePasswordField))
                .perform(clearText(), typeText("password"), closeSoftKeyboard())

        onView(withId(R.id.updateProfile))
                .perform(click())

        onView(withId(R.id.profilePasswordField))
                .check(matches(isDisplayed()))
    }

    @Test
    fun busFragment_shouldWorkCorrectly() {
        goToFragment(AvailableBusesFragment.newInstance())

        Thread.sleep(2000)

        onView(withId(R.id.availableBusesListView))
                .check(matches(isDisplayed()))

        onView(withId(R.id.availableText))
                .check(matches(isDisplayed()))
    }

    @Test
    fun test_C_AddScheduleFragmentShouldWorkAsExpected() {
        // Should not add schedule if fields are empty
        Thread.sleep(1000)
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.addSchedule))

        onView(withId(R.id.addScheduleContainer))
                .check(matches(isDisplayed()))

        onView(withId(R.id.continueBookingButton))
                .perform(click())

//        Thread.sleep(1000)

        onView(withText("All Fields are Required."))
                .check(matches(isDisplayed()))

        // Should add schedule if fields are filled correctly

        fillSchedulesWithValidData("22/04/2050", "10:20")

        onView(withId(R.id.continueBookingButton))
                .perform(click())

        Thread.sleep(2000)
//        onView(withId(R.id.tripFromField))
//                .perform(clearText(), typeText("Lagos"), closeSoftKeyboard())
//
//        onView(withId(R.id.tripToField))
//                .perform(clearText(), typeText("Kampala"), closeSoftKeyboard())
//
//        onView(withId(R.id.departureDateField))
//                .perform(typeText("22/04/2023"))
//
//        Thread.sleep(2000)
//        uiDevice.findObject(UiSelector().text("OK")).click()
//
//        onView(withId(R.id.departureTimeField))
//                .perform(typeText("10:20"))
//
//        Thread.sleep(2000)
//        uiDevice.findObject(UiSelector().text("OK")).click()
//
////        onView(withId(R.id.departureDateField))
////                .perform(PickerActions.setDate(2030, 4, 22))
//
////        uiDevice.findObject(UiSelector().text("OK")).click()
////        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
////                .perform(clearText(),
////                        PickerActions.setDate(2040, 11, 4),
////                        closeSoftKeyboard())
//
////        onView(withId(R.id.departureTimeField))
////                .perform(clearText(),
////                        PickerActions.setTime(10, 25),
////                        closeSoftKeyboard())
//        Thread.sleep(2000)
//        onView(withId(R.id.continueBookingButton))
//                .perform(click())
//
//        Thread.sleep(2000)
    }

    @Test
    fun test_D_UsersShouldBeAbleToAddSchedules() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.addSchedule))

        fillSchedulesWithValidData("27/09/2045", "11:35")

        onView(withId(R.id.continueBookingButton))
                .perform(click())

        Thread.sleep(3000)

        onView(withId(R.id.availableBusesListView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<BusViewHolder>(1, click()))

        Thread.sleep(2000)

        onView(withId(R.id.busSeatList))
                .perform(RecyclerViewActions.actionOnItemAtPosition<BusSeatViewHolder>(1, click()))

        onView(withText("Seat is Unavailable"))
                .check(matches(isDisplayed()))

        Thread.sleep(1000)

        confirmSuccessfulSeatBooking()

//        onView(withId(R.id.busSeatList))
//                .perform(RecyclerViewActions.actionOnItemAtPosition<BusSeatViewHolder>(0, click()))
//
//        Thread.sleep(1000)
//
//        uiDevice.findObject(UiSelector().text("CANCEL")).click()
//
//        Thread.sleep(1000)
//
//        onView(withId(R.id.busSeatList))
//                .perform(RecyclerViewActions.actionOnItemAtPosition<BusSeatViewHolder>(0, click()))
//
//        Thread.sleep(1000)
//
//        uiDevice.findObject(UiSelector().text("CONFIRM")).click()
        Thread.sleep(1000)
    }

    @Test
    fun test_E_confirmOrCancelPaymentShouldWorkPerfectly() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.addSchedule))

        fillSchedulesWithValidData("01/01/2095", "07:45")

        onView(withId(R.id.continueBookingButton))
                .perform(click())

        onView(withId(R.id.availableBusesListView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<BusViewHolder>(1, click()))

        Thread.sleep(2000)

        confirmSuccessfulSeatBooking()

        Thread.sleep(1000)

        uiDevice.findObject(UiSelector().text("MAKE PAYMENT")).click()

        Thread.sleep(1000)

        uiDevice.findObject(UiSelector().text("CANCEL")).click()

        Thread.sleep(1000)

        uiDevice.findObject(UiSelector().text("MAKE PAYMENT")).click()

        Thread.sleep(1000)

        uiDevice.findObject(UiSelector().text("CONFIRM")).click()

        Thread.sleep(1000)

        // Simulate Failed Payment

        uiDevice.findObject(UiSelector().text("CVV")).text = "001"
        uiDevice.findObject(UiSelector().text("Card Number")).text = "4084080000005408"
        uiDevice.findObject(UiSelector().text("Year")).text = "2019"
        uiDevice.findObject(UiSelector().text("Month")).text = "12"

        uiDevice.findObject(UiSelector().text("CONFIRM PAYMENT")).click()

//        uiDevice.findObject(UiSelector().text("cvv")).text = "001"
//        uiDevice.findObject(UiSelector().text("Card number")).text = "4084080000005408"
//        uiDevice.findObject(UiSelector().text("yyyy")).text = "2019"
//        uiDevice.findObject(UiSelector().text("mm")).text = "12"
//
//        Thread.sleep(1000)
//
//        uiDevice.findObject(UiSelector().text("CONTINUE")).click()
//
//        Thread.sleep(3000)

        uiDevice.findObject(UiSelector().text("CLOSE")).click()

        Thread.sleep(1000)

        // Simulate Success

        uiDevice.findObject(UiSelector().text("RETRY")).click()

        uiDevice.findObject(UiSelector().text("CONFIRM")).click()

        uiDevice.findObject(UiSelector().text("CVV")).text = "408"
        uiDevice.findObject(UiSelector().text("Card Number")).text = "4084084084084081"
        uiDevice.findObject(UiSelector().text("Year")).text = "2019"
        uiDevice.findObject(UiSelector().text("Month")).text = "12"


        uiDevice.findObject(UiSelector().text("CONFIRM PAYMENT")).click()
//
//        uiDevice.findObject(UiSelector().text("cvv")).text = "408"
//        uiDevice.findObject(UiSelector().text("Card number")).text = "4084080000005408"
//        uiDevice.findObject(UiSelector().text("yyyy")).text = "2019"
//        uiDevice.findObject(UiSelector().text("mm")).text = "12"
//
//        Thread.sleep(1000)
//
//        uiDevice.findObject(UiSelector().text("CONTINUE")).click()

        Thread.sleep(2000)

        uiDevice.findObject(UiSelector().text("CLOSE")).click()

        Thread.sleep(2000)
        uiDevice.findObject(UiSelector().text("CANCEL PAYMENT")).click()
        Thread.sleep(2000)

        uiDevice.findObject(UiSelector().text("YES, CANCEL")).click()
        Thread.sleep(2000)

        uiDevice.findObject(UiSelector().text("GO TO HOME")).click()
    }

    @Test
    fun test_F_clickingAScheduleShouldLaunchTheSchedulePage() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.recentSchedules))

        onView(withId(R.id.recentScheduleListView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<BusScheduleViewHolder>(0, click()))

        Thread.sleep(3000)
    }
}