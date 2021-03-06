package com.gurunars.item_list.example

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.gurunars.test_utils.DebugActivityRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @get:Rule
    var mActivityRule = DebugActivityRule(ActivityMain::class.java)

    private fun clickMenu(text: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(text)).perform(click())
    }

    @Test
    fun clickingClear_shouldShowEmptyListView() {
        clickMenu("Clear")
        onView(withText("Empty")).check(matches(isDisplayed()))
    }

    private fun assertList(vararg expectedItems: String) {
        for (i in expectedItems.indices) {
            assertEquals(
                expectedItems[i],
                mActivityRule.activity
                    .findViewById<RecyclerView>(R.id.recyclerView)
                    .getChildAt(i)
                    .findViewById<TextView>(R.id.title)
                    .text
            )
        }
    }

    @Test
    fun deletingItems_shouldLeadToPartialRemoval() {
        clickMenu("Delete items")
        assertList(
            "#1{TIGER @ 0}",
            "#3{MONKEY @ 0}"
        )
    }

    @Test
    fun createItems_shouldAppendItemsToTheEnd() {
        clickMenu("Create items")
        Thread.sleep(700)
        assertList(
            "#1{TIGER @ 0}",
            "#2{WOLF @ 0}",
            "#3{MONKEY @ 0}",
            "#4{LION @ 0}",
            "#5{TIGER @ 0}",
            "#6{WOLF @ 0}",
            "#7{MONKEY @ 0}",
            "#8{LION @ 0}"
        )
    }

    @Test
    fun updateItems_shouldChangeSomeOfItems() {
        clickMenu("Update items")
        Thread.sleep(700)
        assertList(
            "#1{TIGER @ 0}",
            "#2{WOLF @ 1}",
            "#3{MONKEY @ 0}",
            "#4{LION @ 1}"
        )
    }

    @Test
    fun moveUp_shouldPutItemFromBottomToTop() {
        clickMenu("Move up")
        assertList(
            "#4{LION @ 0}",
            "#1{TIGER @ 0}",
            "#2{WOLF @ 0}",
            "#3{MONKEY @ 0}"
        )
    }

    @Test
    fun moveDown_shouldPutItemFromTopToBottom() {
        clickMenu("Move down")
        assertList(
            "#2{WOLF @ 0}",
            "#3{MONKEY @ 0}",
            "#4{LION @ 0}",
            "#1{TIGER @ 0}"
        )
    }

    @Test
    fun resetItems_shouldSetItemsToInitialList() {
        clickMenu("Reset items")
        Thread.sleep(700)
        assertList(
            "#1{TIGER @ 0}",
            "#2{WOLF @ 0}",
            "#3{MONKEY @ 0}",
            "#4{LION @ 0}"
        )
    }

    @Before
    fun before() {
        clickMenu("Reset items")
        Thread.sleep(700)
    }

}