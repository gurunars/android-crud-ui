package com.gurunars.floatmenu.example

import android.graphics.Color
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.gurunars.test_utils.DebugActivityRule
import com.gurunars.test_utils.rotate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @get:Rule
    var mActivityRule = DebugActivityRule(ActivityMain::class.java)

    private fun rotate() {
        mActivityRule.rotate()
    }

    private fun fab(): ViewInteraction {
        return onView(withId(R.id.openFab))
    }

    private fun checkNotification(title: String) {
        onView(withId(R.id.notificationView)).check(matches(withText(title)))
        onView(withId(R.id.notificationView)).perform(longClick())
    }

    private fun checkFab(iconDescription: String) {
        onView(withId(R.id.iconView)).check(matches(withContentDescription(iconDescription)))
    }

    @Before
    fun before() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Reset")).perform(click())
    }

    @Test
    fun clickingFab_shouldOpenAndCloseMenu() {
        rotate()
        checkFab("|BG:${Color.YELLOW}|IC:${Color.BLACK}")
        fab().perform(click())
        rotate()
        checkFab("|BG:${Color.WHITE}|IC:${Color.BLACK}")
        fab().perform(click())
        rotate()
        checkFab("|BG:${Color.YELLOW}|IC:${Color.BLACK}")
    }

    @Test
    fun clickingText() {
        onView(withId(R.id.textView)).perform(click())
        checkNotification("Content Text Clicked")
    }

    @Test
    fun clickingButtonInMenu() {
        rotate()
        fab().perform(click())
        rotate()
        onView(withId(R.id.button)).perform(click())
        checkNotification("Menu Button Clicked")
    }

    private fun toggleBg() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Toggle background")).perform(click())
        fab().perform(click())
    }

    @Test
    fun togglingBackground_shouldMakeBackgroundTranslucent() {
        toggleBg()
        rotate()
        onView(withId(R.id.textView)).perform(click())
        checkNotification("Content Text Clicked")
    }

    @Test
    fun togglingBackground_shouldLeaveButtonClickable() {
        toggleBg()
        rotate()
        onView(withId(R.id.button)).perform(click())
        checkNotification("Menu Button Clicked")
    }

    //TODO: flaky
    @Test
    fun togglingBackground_shouldLeaveFrameClickable() {
        toggleBg()
        rotate()
        onView(withId(R.id.buttonFrame)).perform(click())
        checkNotification("Menu Button Frame Clicked")
    }
}