package com.valagroup.scrolltest

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * When [AppBarLayout] is used together with [AppBarLayout.ScrollingViewBehavior]
 * it causes an accessibility issue where the bottom of the view will be cut-off
 * (out of bounds) if we scroll to the bottom of it by only using the keyboard.
 *
 * This is a hack that allows us to collapse/expand the [AppBarLayout] when the
 * user is navigating with keyboard which avoids the issue.
 */
class AppBarLayoutKeyboardHandler(
    private val appBarLayout: AppBarLayout,
    private val scrollBehaviourViewGroup: ViewGroup,
) {
    private val requiredKeyPresses = 2
    private var navigationDownCount = 0
    private var navigationUpCount = 0

    fun handleKeyEvent(event: KeyEvent) {
        if (!scrollBehaviourViewGroup.hasFocusAnywhere()) {
            return
        }
        if (event.shouldHandleNavigationDown()) {
            handleNavigationDown()
        } else if (event.shouldHandleNavigationUp()) {
            handleNavigationUp()
        }
    }

    private fun handleNavigationDown() {
        navigationUpCount = 0
        if (navigationDownCount >= requiredKeyPresses) {
            return
        }
        navigationDownCount++
        if (navigationDownCount >= requiredKeyPresses) {
            appBarLayout.setExpanded(false)
        }
    }

    private fun handleNavigationUp() {
        navigationDownCount = 0
        if (navigationUpCount >= requiredKeyPresses) {
            return
        }
        navigationUpCount++
        if (navigationUpCount >= requiredKeyPresses) {
            appBarLayout.setExpanded(true)
        }
    }

    companion object {
        fun setupIfNeeded(coordinatorLayout: CoordinatorLayout, appBarLayout: AppBarLayout): AppBarLayoutKeyboardHandler? {
            val viewGroup = findViewGroupWithScrollingBehaviour(coordinatorLayout)
            if (viewGroup != null) {
                return AppBarLayoutKeyboardHandler(appBarLayout, viewGroup)
            }
            // If no child with scrolling behaviour is found, we don't need to do anything.
            return null
        }

        /**
         * Find the [ViewGroup] that has [AppBarLayout.ScrollingViewBehavior] defined in it.
         * [AppBarLayout] and a sibling with scrolling behaviour must exist in a parent
         * [CoordinatorLayout], as only only it supports scrolling behaviour.
         */
        private fun findViewGroupWithScrollingBehaviour(coordinatorLayout: CoordinatorLayout): ViewGroup? {
            for (i in 0 until coordinatorLayout.childCount) {
                val child = coordinatorLayout.getChildAt(i)
                if (child !is ViewGroup) {
                    continue
                }
                val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
                if (layoutParams.behavior is AppBarLayout.ScrollingViewBehavior) {
                    return child
                }
            }
            // If no child has ScrollingViewBehavior, we don't need to do anything.
            return null
        }
    }
}

private fun KeyEvent.shouldHandleNavigationDown(): Boolean {
    return action == KeyEvent.ACTION_DOWN
            && (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_PAGE_DOWN)
}

private fun KeyEvent.shouldHandleNavigationUp(): Boolean {
    return action == KeyEvent.ACTION_DOWN
            && (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_PAGE_UP)
}

/**
 * When I tested [ViewGroup.hasFocus] etc, they did not seem to work
 * in all cases, so we need to loop through the layout ourselves.
 */
fun View.hasFocusAnywhere(): Boolean {
    val view = this
    if (view.isFocused || view.hasFocus()) {
        return true
    }
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            if (view.getChildAt(i).hasFocusAnywhere()) {
                return true
            }
        }
    }
    return false
}